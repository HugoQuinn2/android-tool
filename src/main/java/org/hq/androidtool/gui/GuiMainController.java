package org.hq.androidtool.gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.StackPane;
import lombok.Getter;
import org.hq.androidtool.constants.FilesType;
import org.hq.androidtool.constants.Pages;
import org.hq.androidtool.constants.PagesType;
import org.hq.androidtool.device.Device;
import org.hq.androidtool.gui.alert.AlertController;
import org.hq.androidtool.gui.alert.AlertType;
import org.hq.androidtool.menu.MenuController;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class GuiMainController {
    @FXML
    private BorderPane bp_main;
    @FXML
    private StackPane StackPane;
    @FXML
    private ScrollPane pnlContentPane;
    private TabPane pnlPagesContents;
    private List<Page> pages;

    private static final Logger logger = LoggerFactory.getLogger(GuiMainController.class);

    @FXML
    public void initialize() {
        pages = new ArrayList<>();

        try {
            FXMLLoader menu_bar = new FXMLLoader(getClass().getResource(GuiConfig.MENU_BAR_PAGE_PATH));
            menu_bar.setControllerFactory(param -> new MenuController(this));
            bp_main.setLeft(menu_bar.load());
            pnlPagesContents = new TabPane();
            pnlContentPane.setContent(pnlPagesContents);
        } catch (IOException e) {
            logger.error("No se pudo cargar el menu: " + e.getMessage());
        } finally {
            pnlContentPane.setFitToWidth(true);
            pnlContentPane.setFitToHeight(true);
        }
    }

    public <T> void loadContent(String fxmlPath, T object, PagesType pagesType) {
        Page newPage = new Page((Device) object, pagesType, null);
        Page actualPage = getPage(newPage);

        if (actualPage != null) {
            pnlPagesContents.getSelectionModel().select(actualPage.getTab());
        } else {
            try {
                logger.info("Cargando pagina: " + fxmlPath);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                loader.setControllerFactory(param -> {
                    try {
                        return param.getDeclaredConstructor(object.getClass(), GuiMainController.class)
                                .newInstance(object, this);
                    } catch (Exception e) {
                        logger.error("Error en carga de objeto: " + e);
                        throw new RuntimeException(e);
                    }
                });

                Pane content = loader.load();
                Tab tabPage = newPage(content, pagesType, (Device) object);

                tabPage.setOnClosed(event -> {
                    pages.remove(newPage);
                });

                newPage.setTab(tabPage);

                pnlPagesContents.getTabs().add(newPage.getTab());
                pnlPagesContents.getSelectionModel().select(newPage.getTab());
                pages.add(newPage);
            } catch (IOException e) {
                logger.error("No se pudo cargar la pagina: " + e);
                e.printStackTrace();
            }
        }
    }

    private Page getPage(Page newPage) {
        for (Page page: pages) {
            if (newPage.getDevice().equals(page.getDevice())
                    && newPage.getPagesType().equals(page.getPagesType()))
                return page;
        }

        return null;
    }

    private <T> Tab newPage(Pane content, PagesType pagesType, Device device){
        String titlePage = String.format("%s (%s)", Pages.getTitleByType(pagesType), device.getDeviceName());
        Tab tab = new Tab(titlePage);
        FontIcon icon = new FontIcon(Pages.getIconByType(pagesType));

        tab.setContent(content);
        tab.setGraphic(icon);
        return tab;
    }
}
