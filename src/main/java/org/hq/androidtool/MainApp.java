package org.hq.androidtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String app_icon_path = String.valueOf(getClass().getResource("/org/hq/androidtool/layout/icon/AppIcon.png"));
        FXMLLoader gui_main = new FXMLLoader(MainApp.class.getResource("/org/hq/androidtool/layout/MainPage.fxml"));
        Scene scene = new Scene(gui_main.load(), 1100, 750);

        primaryStage.getIcons().add(new Image( app_icon_path ));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Android Tool");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(750);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
