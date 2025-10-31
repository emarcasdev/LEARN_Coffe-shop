module org.example.cofffeshopinterface {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.cofffeshopinterface to javafx.fxml;
    exports org.example.cofffeshopinterface;
}