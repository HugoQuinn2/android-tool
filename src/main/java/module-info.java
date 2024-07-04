module org.hq.androidtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens org.hq.androidtool to javafx.fxml;
    exports org.hq.androidtool;

    opens org.hq.androidtool.gui to javafx.fxml;
}