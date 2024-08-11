package org.hq.androidtool.controllers.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.hq.androidtool.config.GuiConfig;
import org.hq.androidtool.utils.FxmlValidator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiMainController {
    @FXML
    private BorderPane bp_main;
    @FXML
    private ScrollPane pnlContentPane;

    private static final Logger logger = LoggerFactory.getLogger(GuiMainController.class);

    public void initialize() {
        try {
            File MenuBarFile = new File(GuiConfig.MENU_BAR_PAGE_PATH);
            FXMLLoader menu_bar = new FXMLLoader(MenuBarFile.toURI().toURL());

            menu_bar.setControllerFactory(param -> new MenuBarController(this));
            bp_main.setLeft(menu_bar.load());
        } catch (IOException e) {
            logger.error("No se pudo cargar el menu: " + e.getMessage());
        } finally {
            pnlContentPane.setFitToWidth(true);
            pnlContentPane.setFitToHeight(true);
        }
    }

    public <T> void loadContent(String fxmlPath, T object) {
        try {
            logger.info( "Cargando pagina: " + fxmlPath);
            File fxmlFile = new File(fxmlPath);
            if (FxmlValidator.isFxmlSafe(fxmlFile.toURI().toURL())) {
                FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
                loader.setControllerFactory(param -> {
                    try {
                        return param.getDeclaredConstructor(object.getClass()).newInstance(object);
                    } catch (Exception e) {
                        logger.error("Erroe en carga de objeto: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
                Pane content = loader.load();
                pnlContentPane.setContent(content);
            }
        } catch (IOException e) {
            logger.error("No se pudo cargar la pagina: " + e.getMessage());
        }
    }
}
