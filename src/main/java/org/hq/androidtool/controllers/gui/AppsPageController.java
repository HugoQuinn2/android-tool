package org.hq.androidtool.controllers.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.hq.androidtool.controllers.AppsController;
import org.hq.androidtool.models.Application;
import org.hq.androidtool.models.Contact;
import org.hq.androidtool.models.Device;

import java.util.List;

public class AppsPageController {


    @FXML
    public FlowPane pnlApplicationManager;

    private AppsController appsController;
    private Device device;
    private List<Application> applications;

    public AppsPageController(Device device){
        this.device = device;
    }

    @FXML
    public void initialize(){
        appsController = new AppsController();
        this.applications = appsController.getApplications(device);
        System.out.println(applications);
    }

    private void fillData() {
        Task<List<Contact>> task = new Task<>() {
            @Override
            protected List<Contact> call() throws Exception {
                return null;
            }
        };

        task.setOnFailed(e -> {
            Platform.runLater(() -> {
                System.err.println("No se pudo cargar los datos del dispositivo");
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /*private void makeCard(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hq/androidtool/layout/content/AppCard.fxml"));
        Pane itemPane = (Pane) loader.load();
        application_info_card_controller controller = loader.getController();
        controller.definePackage(packageApplication);
    }*/
}
