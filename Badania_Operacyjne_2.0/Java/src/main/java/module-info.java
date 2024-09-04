module com.ja.optimgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
//    requires spring.boot.starter;
//    requires spring.boot.starter.web;
//    requires spring.web;
//    requires spring.webmvc;
//    requires spring.boot.autoconfigure;
//    requires spring.;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.boot.starter.web;
    requires spring.context;
    requires spring.web;
    requires spring.core;
    requires spring.beans;


    opens com.ja.optimgui to javafx.fxml;
    opens com.ja.dataGui;
    exports com.ja.optimgui;
    exports com.ja.model;
}