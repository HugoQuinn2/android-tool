package org.hq.androidtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hq.androidtool.config.GuiConfig;

import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader gui_main = new FXMLLoader(MainApp.class.getResource("/org/hq/androidtool/layout/MainPage.fxml"));
        Scene scene = new Scene(gui_main.load(), GuiConfig.prefWidth, GuiConfig.prefHeight);

        primaryStage.setScene(scene);
        loadConfigGui(primaryStage,scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadConfigGui(Stage primaryStage, Scene scene){
        String style = Objects.requireNonNull(getClass().getResource(GuiConfig.lightTheme)).toExternalForm();
        String appIcon = String.valueOf(getClass().getResource(GuiConfig.appIcon));

        primaryStage.setTitle(GuiConfig.nameApp);
        primaryStage.setMaximized(GuiConfig.maximized);
        primaryStage.setMinWidth(GuiConfig.minWidth);
        primaryStage.setMinHeight(GuiConfig.minHeight);
        primaryStage.getIcons().add(new Image(appIcon));

        scene.getStylesheets().add(style);
    }
}
