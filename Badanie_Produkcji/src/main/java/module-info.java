module com.ja.optimgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens com.ja.optimgui to javafx.fxml;
    exports com.ja.optimgui;
    exports com.ja.model;
}