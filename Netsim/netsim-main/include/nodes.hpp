//
// Created by gberg on 10.01.2023.
//

#ifndef NETSIMV2_NODES_HPP
#define NETSIMV2_NODES_HPP
#include "helpers.hpp"
#include "storage_types.hpp"
#include <map>
#include <memory>
#include <optional>
#include <utility>

enum  class typ_nodes{
    Worker, Storehouse
};

class IPackageReceiver{
public:
    virtual ElementID get_id() const = 0;
    virtual typ_nodes get_receiver_type() const = 0;
    virtual void receive_package(Package&& p) = 0;

    virtual IPackageStockpile::const_iterator cbegin() const = 0;
    virtual IPackageStockpile::const_iterator begin() const= 0;
    virtual IPackageStockpile::const_iterator cend() const = 0;
    virtual IPackageStockpile::const_iterator end() const= 0;
    //virtual ~IPackageReceiver() = default;
};

class ReceiverPreferences{

    using preferences_typ = std::map<IPackageReceiver*, double>;
    using const_iterator = preferences_typ::const_iterator;
    using itertor = preferences_typ::iterator;
public:
    ReceiverPreferences(ProbabilityGenerator pg = probability_generator): pg_(std::move(pg)){};
    preferences_typ const& get_preferences() const {return preferences_;}
    void add_receiver(IPackageReceiver *package_receiver){
        preferences_.emplace(package_receiver,1);
        for(auto& elem : preferences_){
            auto probability_one_item = 1/(double)preferences_.size();
            elem.second = probability_one_item;
        }
    }

    void remove_receiver(IPackageReceiver* package_receiver){
        preferences_.erase(package_receiver);
        if(!preferences_.empty()){
            for(auto& elem : preferences_){
                auto probability_one_item = 1/(double)preferences_.size();
                elem.second = probability_one_item;
            }
        }
    }

    IPackageReceiver *choose_receiver(){
        double selected_receiver = pg_();
        double sum = 0;
        for(auto& elem : preferences_){
            sum += elem.second;
            if(sum > selected_receiver){
                return elem.first;
            }
        }
        return nullptr;
    }

    const_iterator cbegin() const {return preferences_.cbegin();}
    itertor begin() {return preferences_.begin();}
    const_iterator cend() const {return preferences_.cend();}
    itertor end() {return preferences_.end();}

    ~ReceiverPreferences() = default;

private:
    ProbabilityGenerator pg_;
    preferences_typ preferences_;
};

class PackageSender {

private:
    std::optional<Package> buffor_ = std::nullopt;

public:
    PackageSender():receiver_preferences_(probability_generator){};
    PackageSender(PackageSender&) = default;
    PackageSender(PackageSender&&) = default;

    void send_package(){
        if(buffor_){
            receiver_preferences_.choose_receiver()->receive_package(std::move(buffor_.value()));
            buffor_.reset();
        }
    };
    std::optional<Package>& get_sending_buffer() {return buffor_;}
    ReceiverPreferences receiver_preferences_;

protected:
    void push_package(Package&& p){
        buffor_.emplace(std::move(p));
    }
};

class Worker :public IPackageReceiver,public PackageSender{
public:
    Worker(ElementID id, TimeOffset pd, std::unique_ptr<IPackageQueue> q): PackageSender(), id_(id), pd_(pd), queue_(std::move(q)){};

    void receive_package(Package&& p) override{
        queue_->push(std::move(p));
    }

    IPackageQueue* get_queue() const {return queue_.get();}

    ElementID get_id() const override {return id_;}

    typ_nodes get_receiver_type() const override {return typ_nodes::Worker;}

    TimeOffset get_processing_duration() const {return pd_;}
    Time get_package_processing_start_time(){return start_time_;}

    void do_work(Time t){
        if(!buffor_processed and queue_){
            buffor_processed.emplace(queue_->pop());
            start_time_ = t;
        }
        if((t-start_time_ == pd_-1) && buffor_processed){
            push_package(std::move(buffor_processed.value()));
            buffor_processed.reset();
        }

    }

    IPackageStockpile::const_iterator begin() const override{return queue_->begin();}
    IPackageStockpile::const_iterator cbegin() const override{return queue_->cbegin();}
    IPackageStockpile::const_iterator end() const override{return queue_->end();}
    IPackageStockpile::const_iterator cend() const override{return queue_->cend();}

private:
    ElementID id_;
    TimeOffset pd_;
    std::unique_ptr<IPackageQueue> queue_;
    std::optional<Package> buffor_processed = std::nullopt;
    Time start_time_ = 0;
};


class Ramp :public PackageSender{
public:
    Ramp(ElementID id,TimeOffset di):PackageSender(), id_(id), di_(di){};
    Ramp(Ramp&& r) = default;

    TimeOffset get_delivery_interval() const {return di_;}
    ElementID get_id() const {return id_;}

    void deliver_goods(Time t) {
        if ((time_delivered + di_ == t) || t==1) {
            time_delivered = t;
            push_package(Package());
        }
    }

    ~Ramp() = default;
private:
    ElementID id_;
    TimeOffset di_;
    Time time_delivered = 0;
};

class Storehouse :public IPackageReceiver{
public:
    Storehouse(ElementID id, std::unique_ptr<IPackageStockpile> d = std::make_unique<PackageQueue>(PackageQueueType::FIFO)):id_(id), d_(std::move(d)){};
    Storehouse(Storehouse&& ) = default;
    ElementID get_id() const override {return id_;}
    typ_nodes get_receiver_type() const override {return typ_nodes::Storehouse;}


    void receive_package(Package&& p) override{
        d_->push(std::move(p));
    }

    IPackageStockpile::const_iterator cbegin() const override{return d_->cbegin();}
    IPackageStockpile::const_iterator cend() const override {return d_->cend();}
    IPackageStockpile::const_iterator begin() const override {return d_->begin();}
    IPackageStockpile::const_iterator end() const override {return d_->end();}

    //~Storehouse() override = default;

private:
    ElementID id_;
    std::unique_ptr<IPackageStockpile> d_;
};
#endif //NETSIMV2_NODES_HPP
