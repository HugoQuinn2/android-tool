package org.hq.androidtool.gui;

import org.hq.androidtool.adb.*;
import org.hq.androidtool.controllers.AndroidDeviceController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.hq.androidtool.main_app;

import java.io.IOException;

public class menu_bar_controller {
    @FXML
    private Label device_state;
    @FXML
    private Label lbl_app_version;
    @FXML
    private HBox mb_files;
    //@FXML
    //private Button app_settings;
    @FXML
    private Button app_info;
    //@FXML
    //private Button mb_apps;

    private AndroidDeviceController androidDeviceController;
    private gui_main_controller guiMainController;

    public menu_bar_controller(gui_main_controller mainController) {
        this.guiMainController = mainController;
    }
    @FXML
    public void initialize(){
        androidDeviceController = new AndroidDeviceController();
        androidDeviceController.fetchData();

        String deviceModel = androidDeviceController.getModelDevice();

        if (deviceModel == null){
            device_state.setText("No Device");
            mb_files.setDisable(true);
        }else {
            device_state.setText(androidDeviceController.getModelDevice());
        }

        String version = main_app.class.getPackage().getImplementationVersion();
        lbl_app_version.setText("v" + version);
    }


    public void onApp_reload_state(ActionEvent event) {
        androidDeviceController.fetchData();
        String deviceModel = androidDeviceController.getModelDevice();
        String stateDevice = androidDeviceController.getAdbController().getModel().getStateDevice();

        if (stateDevice == "device"){
            device_state.setText(deviceModel);
            mb_files.setDisable(false);
            app_info.setDisable(false);
        }else {
            device_state.setText(stateDevice);
        }
    }

    public void onFile_menu_bar(MouseEvent event){
        System.out.println("prueba");
        guiMainController.loadContent("/org/hq/androidtool/layout/content/files_conets.fxml");
    }
    public void onApps_menu_bar(MouseEvent event){
        System.out.println(androidDeviceController.getAplications());
    }

    public void onInfo_device(ActionEvent event) throws IOException {
        String serialnumber = androidDeviceController.getSerialDevice();
        String mac = androidDeviceController.getMAC();
        String ip = androidDeviceController.getIP();
        String model = androidDeviceController.getModelDevice();

        FXMLLoader popup = new FXMLLoader(getClass().getResource("/org/hq/androidtool/layout/content/popup.fxml"));
        popup.setControllerFactory(param -> new popup_info(androidDeviceController.getAndroidDeviceModel()));
        Parent root = (Parent) popup.load();
        Stage stage = new Stage();
        stage.setTitle("Android Information");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
