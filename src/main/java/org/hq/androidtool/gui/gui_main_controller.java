package org.hq.androidtool.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class gui_main_controller {
    @FXML
    private BorderPane bp_main;

    public void initialize() {
        try {
            FXMLLoader menu_bar = new FXMLLoader(getClass().getResource("/org/hq/androidtool/layout/content/menu_bar.fxml"));
            menu_bar.setControllerFactory(param -> new menu_bar_controller(this));
            bp_main.setLeft(menu_bar.load());
        } catch (IOException e) {
            System.err.println("No se pudo cargar el menu: " + e.getMessage());
        }
    }

    public void loadContent(String fxmlPath) {
        try {
            System.out.println(fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            bp_main.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
