package org.hq.androidtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hq.androidtool.config.GuiConfig;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String style = getClass().getResource(GuiConfig.lightTheme).toExternalForm();

        String app_icon_path = String.valueOf(getClass().getResource("/org/hq/androidtool/layout/icon/AppIcon.png"));
        FXMLLoader gui_main = new FXMLLoader(MainApp.class.getResource("/org/hq/androidtool/layout/MainPage.fxml"));
        Scene scene = new Scene(gui_main.load(), 1100, 750);

        scene.getStylesheets().add(style);

        primaryStage.getIcons().add(new Image( app_icon_path ));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Android Tool");

        primaryStage.setMaximized(GuiConfig.maximized);

        primaryStage.setMinWidth(GuiConfig.minWidth);
        primaryStage.setMinHeight(GuiConfig.minHeight);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
