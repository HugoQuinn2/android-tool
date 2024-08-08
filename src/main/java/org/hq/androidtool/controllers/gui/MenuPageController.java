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

    @FXML
    private ProgressBar pgbStorage;

    @FXML
    private Label lblStorage;

    @FXML
    private Label lblStorageProgress;

    private Device device;
    private DeviceController deviceController;
    private DeviceData deviceData;

    public MenuPageController(Device device){
        this.device = device;
    }

    @FXML
    public void initialize(){
        deviceController = new DeviceController();
        fillData();
    }

    public void fillData(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                deviceData = deviceController.getDeviceData(device);
                Platform.runLater(() -> editData());
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

    private void editData() {
        String[] storage = deviceData.getStorage().split("/");
        double storageUsed = Integer.parseInt(storage[1]);
        double storageTotal = Integer.parseInt(storage[0]);
        double progressStorage = (storageTotal - storageUsed) / storageTotal ;

        String txtStorage = String.format("%s / %s", getReformatStorage( (int) storageUsed), getReformatStorage( (int) storageTotal));

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
        pgbStorage.setProgress(progressStorage);
        lblStorage.setText(txtStorage);
        lblStorageProgress.setText( String.format("%.1f", (progressStorage * 100)) + "% en uso");
    }

    private String getReformatStorage(int storage) {
        if (storage < 1024) {
            return storage + " K";
        } else if (storage < 1048576) {
            return String.format("%.2f MB", (storage / 1024.0) * 10);
        } else if (storage < 1073741824) {
            return String.format("%.2f GB", (storage / 1048576.0) * 10);
        } else {
            return String.format("%.2f TB", (storage / 1073741824.0) * 10);
        }
    }
}
