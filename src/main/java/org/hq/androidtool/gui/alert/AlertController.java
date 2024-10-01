package org.hq.androidtool.gui.alert;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.hq.androidtool.gui.GuiConfig;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

@Setter
public class AlertController {

    private StackPane stackPane;
    private Pane fxmlPane;
    private String fxmlPath;
    private String iconLiteral;

    private AlertPageController alertPageController;

    public AlertController() {
        fxmlPath = GuiConfig.CUSTOM_ALERT;
    }

    public void showNotification(String titleMessage ,String message, int durationInSeconds, AlertType alertType) {
        if (injectFxml()) {
            graphIcon(alertType);
            alertPageController.getNotificationLabel().setText(message);
            alertPageController.getAlertTitle().setText(titleMessage);
            initProgressTime(durationInSeconds);
        }
    }

    private void initProgressTime(int durationInSeconds) {
        ProgressBar pbTimer = alertPageController.getPbTimer();
        pbTimer.setProgress(0);

        int updateIntervalMs = 10;
        int totalUpdates = (durationInSeconds * 1000) / updateIntervalMs;

        new Thread(() -> {
            for (int i = 0; i <= totalUpdates; i++) {
                double progress = (double) i / totalUpdates;
                Platform.runLater(() -> pbTimer.setProgress(progress));
                try {
                    Thread.sleep(updateIntervalMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(this::dropFxml);
        }).start();
    }
    private void graphIcon(AlertType alertType) {
        FontIcon iconAlert = alertPageController.getIconAlert();
        if (iconLiteral == null) {
            iconAlert.setIconLiteral(getDefaultIconLiteral(alertType));
        } else {
            iconAlert.setIconLiteral(iconLiteral);
        }

        alertPageController.getNotificationPane().getStyleClass().add(getClassByAlertType(alertType));
    }
    private String getClassByAlertType(AlertType alertType) {
        return switch (alertType) {
            case ALERT -> "alert";
            case INFO -> "info";
            case ERROR -> "error";
            case SUCCESS -> "success";
        };
    }
    private String getDefaultIconLiteral(AlertType alertType) {
        return switch (alertType) {
            case ALERT -> "bi-exclamation-triangle-fill";
            case INFO -> "bi-info-square";
            case ERROR -> "bi-x-octagon-fill";
            case SUCCESS -> "bi-check-circle-fill";
        };
    }
    private boolean injectFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlPane = loader.load();

            alertPageController = loader.getController();
            alertPageController.setAlertController(this);

            stackPane.setAlignment(fxmlPane, Pos.BOTTOM_RIGHT);
            fxmlPane.setTranslateX(-10);
            fxmlPane.setTranslateY(-10);
            fxmlPane.setId("notificationPane");

            stackPane.getChildren().add(fxmlPane);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void dropFxml() {
        Pane paneToRemove = (Pane) stackPane.lookup("#notificationPane");
        if (paneToRemove != null)
            stackPane.getChildren().remove(paneToRemove);
    }

}
