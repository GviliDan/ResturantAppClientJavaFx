module com.example.resturantfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires java.base;

    opens com.example.resturantfx to javafx.fxml,com.google.gson, java.base;
    exports com.example.resturantfx;
}