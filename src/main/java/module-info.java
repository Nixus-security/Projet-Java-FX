module com.ynov.escape {
    requires javafx.controls;
    requires javafx.graphics;
    requires java.net.http;
    requires com.google.gson;

    opens com.ynov.escape.model to com.google.gson;
    opens com.ynov.escape.service to com.google.gson;

    exports com.ynov.escape;
    exports com.ynov.escape.scene;
    exports com.ynov.escape.model;
}
