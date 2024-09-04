//
// Created by gberg on 10.01.2023.
//
#include "factory.hpp"
#include "nodes.hpp"

enum class Node_colors { UNVISITED, VISITED, VERIFIED };

std::map<const PackageSender*, Node_colors> node_colors;

bool has_reachable_storehouse(const PackageSender& sender, std::map<const PackageSender*, Node_colors>& colors){
    if (colors[&sender] == Node_colors::VERIFIED) {
        return true;
    }
    colors[&sender] = Node_colors::VISITED;

    if (sender.receiver_preferences_.get_preferences().empty()) {
        throw std::exception();
    }

    bool hasOtherReceiver = false;
    for (auto it = sender.receiver_preferences_.cbegin(); it != sender.receiver_preferences_.cend(); it++) {
        const IPackageReceiver& receiver = *it->first;
        if (receiver.get_receiver_type() == typ_nodes::Storehouse) {
            hasOtherReceiver = true;
        } else if (receiver.get_receiver_type() == typ_nodes::Worker) {
            const auto worker = dynamic_cast<const Worker*>(&receiver);
            const auto sendrecv = dynamic_cast<const PackageSender*>(worker);
            if (sendrecv == &sender) {
                continue;
            }
            hasOtherReceiver = true;
            if (colors[sendrecv] == Node_colors::UNVISITED) {
                if (!has_reachable_storehouse(*sendrecv, colors)) {
                    throw std::logic_error("");
                }
            }
        }
    }

    colors[&sender] = Node_colors::VERIFIED;

    if (hasOtherReceiver) {
        return true;
    } else {
        throw std::logic_error("");
    }
}

bool Factory::is_consistent() {
    for (auto it = set_ramp.begin(); it != set_ramp.end(); it++) {
        PackageSender &sender = *it;
        node_colors[&sender] = Node_colors::UNVISITED;
    }
    for (auto it = set_worker.begin(); it != set_worker.end(); it++) {
        PackageSender &sender = *it;
        node_colors[&sender] = Node_colors::UNVISITED;
    }

    try {
        for (auto it = set_ramp.begin(); it != set_ramp.end(); it++) {
            PackageSender &sender = *it;
            if (has_reachable_storehouse(sender, node_colors)) {}
        }
        for (auto it = set_worker.begin(); it != set_worker.end(); it++) {
            PackageSender &sender = *it;
            if (has_reachable_storehouse(sender, node_colors)) {}
        }

    }catch (...) {
        return false;  // Network is not connected.
    }
    return true;
}

std::vector<std::string> replace_line_on_token(std::string line,char delimiter){
    std::vector<std::string> tokens;
    std::string token;
    std::istringstream token_stream(line);
    while(std::getline(token_stream,token,delimiter)){
        tokens.push_back(token);
    }
    return tokens;
}

ElementType check_first_token(std::string first_token){
    if(first_token=="LOADING_RAMP" || first_token=="ramp"){
        return ElementType::LOADING_RAMP;
    }else if(first_token=="WORKER" || first_token=="worker"){
        return ElementType::WORKER;
    }else if(first_token=="STOREHOUSE" || first_token == "storehouse"){
        return ElementType::STOREHOUSE;
    }else if(first_token=="LINK"){
        return ElementType::LINK;
    }else{
        throw std::invalid_argument("error, wrong origin");
    }
}

ParsedLineData parse_line(std::string line){
    ParsedLineData result;
    std::vector<std::string> tokens = replace_line_on_token(line,' ');

    for(auto& elem: tokens){
        if(elem == tokens[0]){
            result.elementType = check_first_token(tokens[0]);
        }else{
            std::vector<std::string> parameters_node = replace_line_on_token(elem,'=');
            result.parameters[parameters_node[0]] = parameters_node[1];
        }
    }

    return result;
}

Factory load_factory_structure(std::istream& is){
    Factory result_factory;
    if(is.good()){
        std::string line;
        while(std::getline(is, line)){
            if(line.empty() || line[0]==';'){
                continue;
            }else{
                ParsedLineData parsedLineData = parse_line(line);
                switch (parsedLineData.elementType) {
                    case ElementType::LOADING_RAMP:{
                        Ramp rampa(std::stoi(parsedLineData.parameters["id"]), std::stoi(parsedLineData.parameters["delivery-interval"]));
                        result_factory.add_ramp(std::move(rampa));
                        break;
                    }
                    case ElementType::WORKER: {
                        PackageQueueType type_queue;
                        if (parsedLineData.parameters["queue-type"] == "FIFO") {
                            type_queue = PackageQueueType::FIFO;
                        } else if (parsedLineData.parameters["queue-type"] == "LIFO") {
                            type_queue = PackageQueueType::LIFO;
                        }
                        Worker worker(std::stoi(parsedLineData.parameters["id"]),
                                      std::stoi(parsedLineData.parameters["processing-time"]),
                                      std::make_unique<PackageQueue>(type_queue));
                        result_factory.add_worker(std::move(worker));
                        break;
                    }
                    case ElementType::STOREHOUSE: {
                        Storehouse storehouse(std::stoi(parsedLineData.parameters["id"]));
                        result_factory.add_storehouse(std::move(storehouse));
                        break;
                    }
                    case ElementType::LINK: {
                        std::vector<std::string> source_node = replace_line_on_token(parsedLineData.parameters["src"],'-');
                        std::vector<std::string> destynation_node = replace_line_on_token(parsedLineData.parameters["dest"],'-');
                        IPackageReceiver* receiver_to_add = nullptr;

                        if(destynation_node[0] == "worker"){
                            receiver_to_add = &(*result_factory.find_worker_by_id(std::stoi(destynation_node[1])));
                        }else if(destynation_node[0] == "store"){
                            receiver_to_add = &(*result_factory.find_storehouse_by_id(std::stoi(destynation_node[1])));
                        }

                        if(source_node[0] == "ramp"){
                            result_factory.find_ramp_by_id(std::stoi(source_node[1]))->receiver_preferences_.add_receiver(receiver_to_add);
                        }else if(source_node[0] == "worker"){
                            result_factory.find_worker_by_id(std::stoi(source_node[1]))->receiver_preferences_.add_receiver(receiver_to_add);
                        }

                        break;
                    }
                }
            }
        }
    }else{
        std::cout <<"Failed to open file" << std::endl;
    }


    return result_factory;
}

void save_factory_structure(const Factory& factory, std::ostringstream& oss){

    std::vector<std::string> output;
    std::string pusta_linijka = "\n";
    std::string sloadingramps = "; == LOADING RAMPS ==\n";
    std::string sworkers = "; == WORKERS ==\n";
    std::string sstorehauses = "; == STOREHOUSES ==\n";
    std::string slinks = "; == LINKS ==\n";

    output.push_back(sloadingramps);
    output.push_back(pusta_linijka);

    for (auto it = factory.ramp_cbegin(); it != factory.ramp_cend(); it++) {
        const Ramp& rampa = *it;
        const auto id = rampa.get_id();
        const auto di = rampa.get_delivery_interval();
        char line[100];
        sprintf(line ,"LOADING_RAMP id=%i delivery-interval=%i\n", id, di);
        output.push_back(line);
    }

    output.push_back(pusta_linijka);
    output.push_back(sworkers);
    output.push_back(pusta_linijka);

    for (auto it = factory.worker_cbegin(); it != factory.worker_cend(); it++) {
        Worker& worker = const_cast<Worker &>(*it);
        const auto id = worker.get_id();
        const auto pt = worker.get_processing_duration();
        const auto qt = worker.get_queue()->get_queue_type();
        char line[100];
        if(qt == PackageQueueType::LIFO){
            sprintf(line ,"WORKER id=%i processing-time=%i queue-type=LIFO\n", id, pt);
        }else{
            sprintf(line ,"WORKER id=%i processing-time=%i queue-type=FIFO\n", id, pt);
        }
        output.push_back(line);
    }

    output.push_back(pusta_linijka);
    output.push_back(sstorehauses);
    output.push_back(pusta_linijka);

    for (auto it = factory.storehouse_cbegin(); it != factory.storehouse_cend(); it++) {
        const Storehouse& store = *it;
        const auto id = store.get_id();
        char line[100];
        sprintf(line ,"STOREHOUSE id=%i\n", id);
        output.push_back(line);
    }

    output.push_back(pusta_linijka);
    output.push_back(slinks);
    output.push_back(pusta_linijka);

    for (auto it = factory.ramp_cbegin(); it != factory.ramp_cend(); it++) {
        const Ramp& rampa = *it;
        const auto id = rampa.get_id();
        const auto wid = rampa.receiver_preferences_.get_preferences();
        for (auto it2 = rampa.receiver_preferences_.cbegin(); it2 != rampa.receiver_preferences_.cend(); it2++) {
            const IPackageReceiver& receiver = *it2->first;
            const auto rid= receiver.get_id();
            char line[100];
            sprintf(line ,"LINK src=ramp-%i dest=worker-%i\n", id, rid);
            output.push_back(line);
        }
        output.push_back(pusta_linijka);
    }

    for (auto it = factory.worker_cbegin(); it != factory.worker_cend(); it++) {
        const Worker& worker = *it;
        const auto id = worker.get_id();
        const auto wid = worker.receiver_preferences_.get_preferences();
        for (auto it2 = worker.receiver_preferences_.cbegin(); it2 != worker.receiver_preferences_.cend(); it2++) {
            const IPackageReceiver& receiver = *it2->first;
            const auto rid= receiver.get_id();
            const auto wor_store = receiver.get_receiver_type();
            if (wor_store == typ_nodes::Worker){
                char line[100];
                sprintf(line ,"LINK src=worker-%i dest=worker-%i\n", id, rid);
                output.push_back(line);
            }else{
                char line[100];
                sprintf(line ,"LINK src=worker-%i dest=store-%i\n", id, rid);
                output.push_back(line);
            }


        }
        output.push_back(pusta_linijka);
    }


    for (const auto &str : output) {
        oss << str;
    }
}
