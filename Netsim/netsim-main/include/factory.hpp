//
// Created by gberg on 10.01.2023.
//

#ifndef NETSIMV2_FACTORY_HPP
#define NETSIMV2_FACTORY_HPP

#include "nodes.hpp"
#include "types.hpp"


template<class Node>
class NodeCollection{
public:
    using container_t = typename std::list<Node>;
    using iterator = typename container_t::iterator;
    using const_iterator = typename container_t::const_iterator;

    void add(Node&& node){node_collection.push_back(std::move(node));}

    void remove_by_id(ElementID id){
        node_collection.erase(find_by_id(id));
    }

    NodeCollection<Node>::iterator find_by_id(ElementID id){
        return std::find_if(begin(),end(),[id](Node& node){return (node.get_id() == id);});
    }

    NodeCollection<Node>::const_iterator find_by_id(ElementID id) const{
        return std::find_if(cbegin(),cend(),[id](const Node& node){return (node.get_id() == id);});
    }

    const_iterator cbegin() const {return node_collection.cbegin(); }
    const_iterator cend() const {return node_collection.cend();}
    iterator begin() {return node_collection.begin();}
    iterator end() {return node_collection.end();}
    const_iterator begin() const {return node_collection.cbegin();}
    const_iterator end() const {return node_collection.cend();}



private:
    container_t node_collection;
};

class Factory{
public:
    bool is_consistent();

    void add_ramp(Ramp&& r){set_ramp.add(std::move(r));}
    void remove_ramp(ElementID id){
        if(find_ramp_by_id(id)!=ramp_cend()) {
            set_ramp.remove_by_id(id);
        }
    }
    NodeCollection<Ramp>::iterator find_ramp_by_id(ElementID id){return set_ramp.find_by_id(id);}
    NodeCollection<Ramp>::const_iterator find_ramp_by_id(ElementID id) const {return set_ramp.find_by_id(id);}
    NodeCollection<Ramp>::const_iterator ramp_cbegin() const {return set_ramp.cbegin();}
    NodeCollection<Ramp>::const_iterator ramp_cend() const {return set_ramp.cend();}

    void add_worker(Worker&& w){set_worker.add(std::move(w));}
    NodeCollection<Worker>::iterator find_worker_by_id(ElementID id){return set_worker.find_by_id(id);}
    NodeCollection<Worker>::const_iterator find_worker_by_id(ElementID id) const {return set_worker.find_by_id(id);}
    void remove_worker(ElementID id){
        remove_receiver(set_ramp,id);
        if(find_worker_by_id(id)!=worker_cend()) {
            set_worker.remove_by_id(id);
        }
    }
    NodeCollection<Worker>::const_iterator worker_cbegin() const {return set_worker.cbegin();}
    NodeCollection<Worker>::const_iterator worker_cend() const {return set_worker.cend();}

    void add_storehouse(Storehouse&& s){set_storehouse.add(std::move(s));}
    void remove_storehouse(ElementID id){
        remove_receiver(set_worker,id);
        if(find_storehouse_by_id(id) != storehouse_cend()) {
            set_storehouse.remove_by_id(id);
        }
    }
    NodeCollection<Storehouse>::iterator find_storehouse_by_id(ElementID id){return set_storehouse.find_by_id(id);}
    NodeCollection<Storehouse>::const_iterator find_storehouse_by_id(ElementID id) const {return set_storehouse.find_by_id(id);}
    NodeCollection<Storehouse>::const_iterator storehouse_cbegin() const {return set_storehouse.cbegin();}
    NodeCollection<Storehouse>::const_iterator storehouse_cend() const {return set_storehouse.cend();}

    void do_deliveries(Time t){
        for(auto& elem: set_ramp){
            elem.deliver_goods(t);
        }
    }

    void do_work(Time t){
        for(auto& elem: set_worker){
            elem.do_work(t);
        }
    }

    void do_package_passing(){
        for(auto& elem: set_ramp){
            elem.send_package();
        }

        for(auto& elem: set_worker){
            elem.send_package();
        }
    }

private:

    template<class Node>
    void remove_receiver(NodeCollection<Node>& collection, ElementID id){
        IPackageReceiver* receiver_to_remove = nullptr;

        if(typeid(collection) == typeid(set_ramp)){
            for(auto& worker: set_worker){
                if(worker.get_id()==id){
                    receiver_to_remove = &worker;
                }
            }
        }else if(typeid(collection) == typeid(set_worker)){
            for(auto& storehouse: set_storehouse){
                if(storehouse.get_id()==id){
                    receiver_to_remove = &storehouse;
                }
            }
        }

        for(auto& elem: collection) {
            elem.receiver_preferences_.remove_receiver(receiver_to_remove);
        }
    }

    NodeCollection<Ramp> set_ramp;
    NodeCollection<Worker> set_worker;
    NodeCollection<Storehouse> set_storehouse;
};

enum class ElementType{
    LOADING_RAMP,
    WORKER,
    STOREHOUSE,
    LINK
};

struct ParsedLineData{
    ElementType elementType;
    std::map<std::string, std::string> parameters;
};

std::vector<std::string> replace_line_on_token(std::string line,char delimiter);

ElementType check_first_token(std::string first_token);

ParsedLineData parse_line(std::string line);

Factory load_factory_structure(std::istream& is);

void save_factory_structure(const Factory&, std::ostringstream& oss);


#endif //NETSIMV2_FACTORY_HPP