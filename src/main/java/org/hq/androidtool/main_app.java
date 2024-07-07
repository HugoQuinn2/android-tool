package org.hq.androidtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class main_app extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String app_icon_path = String.valueOf(getClass().getResource("/org/hq/androidtool/layout/icon/app_icon.png"));
        FXMLLoader gui_main = new FXMLLoader(main_app.class.getResource("/org/hq/androidtool/layout/gui-main.fxml"));
        Scene scene = new Scene(gui_main.load(), 1300, 750);

        primaryStage.getIcons().add(new Image( app_icon_path ));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Android Tool");
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(550);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
