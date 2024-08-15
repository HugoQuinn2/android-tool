package org.hq.androidtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hq.androidtool.config.GuiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

public class MainApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Ejecutando Aplicacion");

        FXMLLoader guiMain = new FXMLLoader(getClass().getResource(GuiConfig.MAIN_PAGE_PATH));

        Scene scene = new Scene(guiMain.load(), GuiConfig.prefWidth, GuiConfig.prefHeight);

        primaryStage.setScene(scene);
        loadConfigGui(primaryStage,scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadConfigGui(Stage primaryStage, Scene scene){
        String style = Objects.requireNonNull(getClass().getResource(GuiConfig.lightTheme)).toExternalForm();

        primaryStage.setTitle(GuiConfig.nameApp);
        primaryStage.setMaximized(GuiConfig.maximized);
        primaryStage.setMinWidth(GuiConfig.minWidth);
        primaryStage.setMinHeight(GuiConfig.minHeight);
//        primaryStage.getIcons().add(new Image(GuiConfig.appIcon));

        scene.getStylesheets().add(style);
    }
}
