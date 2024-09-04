//
// Created by gberg on 10.01.2023.
//

#include <list>
#include <iostream>
#include <iomanip>
#include <string>
#include <set>

#ifndef NETSIM_NODES_H
#define NETSIM_NODES_H

#include "types.hpp"
#include "package.hpp"


enum class PackageQueueType{
    FIFO,
    LIFO
};



class IPackageStockpile{
public:
    virtual void push(Package&& package_) = 0;
    virtual bool empty() = 0;
    virtual size_type size() = 0;

    using const_iterator = std::list<Package>::const_iterator;

    virtual const_iterator cbegin() const = 0;
    virtual const_iterator cend() const = 0;
    virtual std::list<Package>::iterator begin() = 0;
    virtual std::list<Package>::iterator end() = 0;

    virtual ~IPackageStockpile() = default;
};

class IPackageQueue :public IPackageStockpile{
public:
    virtual Package pop() = 0;
    virtual PackageQueueType get_queue_type() = 0;

};

class PackageQueue :public IPackageQueue{
public:

    PackageQueue (PackageQueueType type_queue): type_queue_(type_queue){}

    void push(Package&& package_) override {product_container_.emplace_back(std::move(package_));}

    bool empty() override {return product_container_.empty();}
    size_type size() override {return product_container_.size();}


    const_iterator cbegin() const override {return product_container_.cbegin();}
    const_iterator cend() const override {return product_container_.cend();}
    std::list<Package>::iterator begin() override {return product_container_.begin();}
    std::list<Package>::iterator end() override {return product_container_.end();}

    Package pop() override;
    PackageQueueType get_queue_type() override;

private:
    PackageQueueType type_queue_;
    std::list<Package> product_container_;
};

#endif //NETSIM_NODES_H
