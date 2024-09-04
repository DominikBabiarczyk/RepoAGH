#include <iostream>
#include <fstream>
#include "ostream"
#include "storage_types.hpp"
#include "package.hpp"
#include "helpers.hpp"
#include "nodes.hpp"
#include "factory.hpp"


int main() {
    /*
    Factory factory;
    factory.add_ramp(Ramp(1, 1));
    factory.add_worker(Worker(1, 1, std::make_unique<PackageQueue>(PackageQueueType::FIFO)));
    factory.add_worker(Worker(2, 1, std::make_unique<PackageQueue>(PackageQueueType::LIFO)));
    factory.add_storehouse(Storehouse(1));

    Ramp& r1 = *(factory.find_ramp_by_id(1));
    Worker& w1 = *(factory.find_worker_by_id(1));
    Storehouse& s1 = *(factory.find_storehouse_by_id(1));
    r1.receiver_preferences_.add_receiver(&w1);
    w1.receiver_preferences_.add_receiver(&s1);

    Worker& w2 = *(factory.find_worker_by_id(2));
    r1.receiver_preferences_.add_receiver(&w2);
    w2.receiver_preferences_.add_receiver(&s1);
    std::ostringstream oss;

    save_factory_structure(factory, oss);
    std::cout<<oss.str();*/
}
