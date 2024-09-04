//
// Created by gberg on 10.01.2023.
//

#ifndef NETSIMV2_TYPES_HPP
#define NETSIMV2_TYPES_HPP

#include <functional>

using ElementID = int;
using size_type = int;
using TimeOffset = int;
using Time = int;

using ProbabilityGenerator = std::function<double()>;


#endif //NETSIMV2_TYPES_HPP
