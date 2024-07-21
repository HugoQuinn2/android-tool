package org.hq.androidtool.controllers.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GuiMainController {
    @FXML
    private BorderPane bp_main;
    @FXML
    private ScrollPane pnlContentPane;

    public void initialize() {
        try {
            FXMLLoader menu_bar = new FXMLLoader(getClass().getResource("/org/hq/androidtool/layout/content/MenuBar.fxml"));
            menu_bar.setControllerFactory(param -> new MenuBarController(this));
            bp_main.setLeft(menu_bar.load());
        } catch (IOException e) {
            System.err.println("No se pudo cargar el menu: " + e.getMessage());
        }

        pnlContentPane.setFitToWidth(true);
        pnlContentPane.setFitToHeight(true);
    }

    public <T> void loadContent(String fxmlPath, T object) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(param -> {
                try {
                    return param.getDeclaredConstructor(object.getClass()).newInstance(object);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Pane content = loader.load();
            pnlContentPane.setContent(content);
            //bp_main.setCenter(loader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
