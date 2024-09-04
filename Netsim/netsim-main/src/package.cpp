//
// Created by gberg on 10.01.2023.
//

#include "package.hpp"

Package::Package() {
    if(!freed_IDs.empty()) {
        id_ = *(freed_IDs.begin());
        freed_IDs.erase(*(freed_IDs.begin()));
        assigned_IDs.insert(*(freed_IDs.begin()));
    }else{
        id_ = (*(assigned_IDs.rbegin())) + 1;
        assigned_IDs.insert((*(assigned_IDs.rbegin())) + 1);
    }

}

Package::~Package() {
    freed_IDs.insert(id_);
    assigned_IDs.erase(id_);
}