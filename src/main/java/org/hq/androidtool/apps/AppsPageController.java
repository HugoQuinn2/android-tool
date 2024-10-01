package org.hq.androidtool.apps;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hq.androidtool.constants.Messages;
import org.hq.androidtool.gui.GuiConfig;
import org.hq.androidtool.apps.card.AppCardController;
import org.hq.androidtool.device.Device;
import org.hq.androidtool.gui.GuiMainController;
import org.hq.androidtool.gui.alert.AlertController;
import org.hq.androidtool.gui.alert.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AppsPageController {
    @FXML
    private Button btnDownloadApp;
    @FXML
    private Button btnDropApp;
    @FXML
    private Button btnInstallApp;
    @FXML
    public FlowPane pnlApplicationManager;

    @FXML
    public TextField txtFilter;

    private AppsController appsController;
    private Device device;
    private List<Application> applications;
    private List<Pane> appCardPaneList;
    private Map<Pane, Object> paneControllerMap;
    private AppCardController cardChose;
    private DropShadow shadow;

    private static final Logger logger = LoggerFactory.getLogger(AppsPageController.class);
    private GuiMainController guiMainController;
    private AlertController alertController;


    public AppsPageController(Device device, GuiMainController guiMainController){
        this.device = device;
        this.guiMainController = guiMainController;

        this.alertController = new AlertController();
        alertController.setStackPane(guiMainController.getStackPane());

        this.appCardPaneList = new ArrayList<>();
        this.paneControllerMap  = new HashMap<>();

        this.shadow = new DropShadow();
        shadow.setColor(javafx.scene.paint.Color.web("#1C1C1C"));
        shadow.setRadius(10);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);

    }

    @FXML
    public void initialize(){
        appsController = new AppsController(this.device);
        btnDropApp.setDisable(true);
        btnDownloadApp.setDisable(true);

        fillData();
        initFilter();
    }

    public void onBtnDropApp(MouseEvent event){
        if (cardChose != null) {
            String appName = cardChose.getApplication().getName();
            String appPackage = cardChose.getApplication().getPackageName();

            if (showDeleteConfirmationDialog(cardChose.getApplication().getName())) {
                if (appsController.dropApp(appPackage)) {
                    showMessageDrop(AlertType.SUCCESS, appName);
                    fillData();
                } else {
                    showMessageDrop(AlertType.ERROR, appName);
                }
            }
        }
    }

    public void onBtnDownloadApp(MouseEvent event) {
        if (cardChose != null) {
            String appName = cardChose.getApplication().getName();
            String appPackage = cardChose.getApplication().getPackageName();

            File file = (new DirectoryChooser()).showDialog(new Stage());

            if (file != null) {
                showMessageDownload(AlertType.INFO, appName);
                String toPath = file.getAbsolutePath() + File.separator + appName + ".apk";
                if (appsController.pullBaseApk(appPackage, toPath)) {
                    showMessageDownload(AlertType.SUCCESS, appName);
                } else {
                    showMessageDownload(AlertType.ERROR, appName);
                }
            }
        }
    }

    public void onBtnInstallApp(MouseEvent event) {
        File file = (new FileChooser()).showOpenDialog(new Stage());

        if (file != null) {
            String apkPath = file.getAbsolutePath();
            String apkName = file.getName();

//            showMessageInstall(AlertType.INFO, apkName);
            if (appsController.installApk(apkPath))
                showMessageInstall(AlertType.SUCCESS, apkName);
            else
                showMessageInstall(AlertType.ERROR, apkName);
        }
    }

    private static boolean showDeleteConfirmationDialog(String itemName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que quieres eliminar " + itemName + "?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void fillData() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                logger.info(  device.getDeviceName() + " | " + "Cargando Aplicaciones de dispositivo.");
                for (Application application : applications) {
                    Pane pane = makeAppCard(application);
                    if (pane != null) {
                        appCardPaneList.add(pane);
                    }
                }
                Platform.runLater(() -> pnlApplicationManager.getChildren().addAll(appCardPaneList));
                return null;
            }
        };

        task.setOnFailed(e -> {
            Platform.runLater(() -> {
                logger.error(  device.getDeviceName() + " | " + "No se pudo cargar las aplicacion de dispositivo: " + e);
            });
        });

        applications = appsController.getApplications();
        pnlApplicationManager.getChildren().clear();

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    private void initFilter() {
        txtFilter.setPromptText("Buscar...");
        txtFilter.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String filterText = txtFilter.textProperty().get().toLowerCase();
                pnlApplicationManager.getChildren().clear();

                if (filterText.isEmpty()){
                    pnlApplicationManager.getChildren().addAll(appCardPaneList);
                    return;
                }

                List<Pane> newApplicationObservableList = new ArrayList<>();

                for (Pane appCard : appCardPaneList){
                    AppCardController controller = (AppCardController) getController(appCard);
                    if (controller != null) {
                        String appName = controller.getApplication().getName();
                        String appPackage = controller.getApplication().getPackageName();

                        if (appName.contains(filterText) || appPackage.contains(filterText)) {
                            newApplicationObservableList.add(appCard);
                        }
                    }

                }

                pnlApplicationManager.getChildren().addAll(newApplicationObservableList);
            }
        });
    }

    private Pane makeAppCard(Application application){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(GuiConfig.APP_CARD_PATH));
            loader.setControllerFactory(param -> new AppCardController(application));
            Pane pane = loader.load();
            AppCardController controller = loader.getController();
            paneControllerMap.put(pane, controller);

            pane.setOnMouseClicked(event -> handleAppCardClick(pane));

            return pane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Object getController(Pane pane) {
        return paneControllerMap.get(pane);
    }

    private void handleAppCardClick(Pane pane) {
        btnDropApp.setDisable(false);
        btnDownloadApp.setDisable(false);
        for (Pane appCard : appCardPaneList) {
            appCard.getStyleClass().remove("card-selected");
        }
        pane.getStyleClass().add("card-selected");

        this.cardChose = (AppCardController) getController(pane);
    }

    private void showMessageDrop(AlertType alertType, String... data) {
        if (alertType.equals(AlertType.SUCCESS))
            alertController.showNotification(
                    Messages.DROP_APP_SUCCESS.getTitle(),
                    Messages.DROP_APP_SUCCESS.getMessage(data),
                    10,
                    alertType
            );
        if (alertType.equals(AlertType.ERROR))
            alertController.showNotification(
                    Messages.DROP_APP_ERROR.getTitle(),
                    Messages.DROP_APP_ERROR.getMessage(data),
                    10,
                    alertType
            );
    }
    private void showMessageInstall(AlertType alertType, String... data) {
        if (alertType.equals(AlertType.SUCCESS))
            alertController.showNotification(
                    Messages.INSTALL_APP_SUCCESS.getTitle(),
                    Messages.INSTALL_APP_SUCCESS.getMessage(data),
                    5,
                    alertType
            );
        if (alertType.equals(AlertType.ERROR))
            alertController.showNotification(
                    Messages.INSTALL_APP_ERROR.getTitle(),
                    Messages.INSTALL_APP_ERROR.getMessage(data),
                    5,
                    alertType
            );
        if (alertType.equals(AlertType.INFO))
            alertController.showNotification(
                    Messages.INSTALL_APP_INFO.getTitle(),
                    Messages.INSTALL_APP_INFO.getMessage(data),
                    5,
                    alertType
            );
    }
    private void showMessageDownload(AlertType alertType, String... data) {
        if (alertType.equals(AlertType.SUCCESS))
            alertController.showNotification(
                    Messages.DOWNLOAD_APP_SUCCESS.getTitle(),
                    Messages.DOWNLOAD_APP_SUCCESS.getMessage(data),
                    5,
                    alertType
            );
        if (alertType.equals(AlertType.ERROR))
            alertController.showNotification(
                    Messages.DOWNLOAD_APP_ERROR.getTitle(),
                    Messages.DOWNLOAD_APP_ERROR.getMessage(data),
                    5,
                    alertType
            );
        if (alertType.equals(AlertType.INFO))
            alertController.showNotification(
                    Messages.DOWNLOAD_APP_INFO.getTitle(),
                    Messages.DOWNLOAD_APP_INFO.getMessage(data),
                    5,
                    alertType
            );
    }

}
