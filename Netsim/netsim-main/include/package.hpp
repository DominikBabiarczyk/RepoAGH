//
// Created by gberg on 10.01.2023.
//

#ifndef NETSIMV2_PACKAGE_HPP
#define NETSIMV2_PACKAGE_HPP

#include "types.hpp"
#include <list>
#include <iostream>
#include <iomanip>
#include <string>
#include <set>


class Package{

public:
    Package();
    Package(ElementID id):id_(id){};
    Package(Package&&) = default;
    Package(const Package&) = default;
    ElementID get_id() const {return id_;}
    Package& operator=(Package&&) = default;
    ~Package();

private:
    ElementID id_;
    inline static std::set<ElementID> assigned_IDs{0};
    inline static std::set<ElementID> freed_IDs;
    static const ElementID BLANK_ID = -1;

};
#endif //NETSIMV2_PACKAGE_HPP