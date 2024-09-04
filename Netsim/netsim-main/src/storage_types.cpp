//
// Created by gberg on 10.01.2023.
//

#include "storage_types.hpp"

Package PackageQueue::pop(){
    if(type_queue_ == PackageQueueType::LIFO){
        Package p(std::move(product_container_.back()));
        product_container_.pop_back();
        return p;
    }else if(type_queue_ == PackageQueueType::FIFO) {
        Package p(std::move(product_container_.front()));
        product_container_.pop_front();
        return p;
    }
    throw std::logic_error("");
}

PackageQueueType PackageQueue::get_queue_type() {
    switch(type_queue_){
        case PackageQueueType::LIFO:
            //std::cout << "LIFO" << std::endl;
            return PackageQueueType::LIFO;
            break;

        case PackageQueueType::FIFO:
            //std::cout << "FIFO" << std::endl;
            return PackageQueueType::FIFO;
            break;
    }
    throw std::logic_error("");
}