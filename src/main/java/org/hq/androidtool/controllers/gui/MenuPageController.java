package org.hq.androidtool.controllers.gui;

import javafx.concurrent.Task;
import org.hq.androidtool.controllers.DeviceController;
import org.hq.androidtool.models.Device;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hq.androidtool.models.DeviceData;
import javafx.application.Platform;

public class MenuPageController {
    @FXML
    private TextField lblAndroidId;

    @FXML
    private Label lblAndroidVersion;

    @FXML
    private TextField lblDisplay;

    @FXML
    private TextField lblIp;

    @FXML
    private TextField lblMac;

    @FXML
    private Label lblManufacturer;

    @FXML
    private Label lblModel;

    @FXML
    private TextField lblProcessor;

    @FXML
    private TextField lblRam;

    @FXML
    private TextField lblSerial;

    @FXML
    private Label lblSimContract;

    private Device device;
    private DeviceController deviceController;
    private DeviceData deviceData;

    public MenuPageController(Device device){
        this.device = device;
    }

    @FXML
    public void initialize(){
        deviceController = new DeviceController();
        deviceData = deviceController.getDeviceData( this.device );
        fillData();
    }

    public void fillData(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                lblAndroidVersion.setText( "Android " + deviceData.getAndroidVersion());
                lblIp.setText(deviceData.getIpDevice());
                lblMac.setText(deviceData.getMacDevice());
                lblManufacturer.setText(deviceData.getManufacturer());
                lblModel.setText(deviceData.getDeviceModel());
                lblSerial.setText(deviceData.getSerialNumber());
                lblSimContract.setText(deviceData.getSimContract());
                lblAndroidId.setText(deviceData.getAndroidId());
                lblDisplay.setText(deviceData.getDisplaySize());
                lblProcessor.setText(deviceData.getProcessor());
                lblRam.setText(deviceData.getRam());
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
}
