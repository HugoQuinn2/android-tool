module org.hq.androidtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires javafx.base;
    requires javafx.graphics;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    // add icon pack modules
    requires org.kordamp.ikonli.bootstrapicons;
    requires java.desktop;
    requires java.net.http;
    requires com.google.gson;
    requires org.controlsfx.controls;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires org.jsoup;


    opens org.hq.androidtool to javafx.fxml;
    exports org.hq.androidtool;

    opens org.hq.androidtool.utils to javafx.fxml;
    opens org.hq.androidtool.services to javafx.fxml;
    opens org.hq.androidtool.constants to javafx.fxml;
    opens org.hq.androidtool.files to javafx.base, javafx.fxml;
    opens org.hq.androidtool.menu to javafx.fxml;
    opens org.hq.androidtool.apps.card to javafx.fxml;
    opens org.hq.androidtool.gui to javafx.fxml;
    opens org.hq.androidtool.adb to javafx.fxml;
    opens org.hq.androidtool.device to javafx.base, javafx.fxml;
    opens org.hq.androidtool.apps to javafx.base, javafx.fxml;
    opens org.hq.androidtool.contacts to javafx.base, javafx.fxml;
    opens org.hq.androidtool.gui.alert to javafx.base, javafx.fxml;
}