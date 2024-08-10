package org.hq.androidtool.controllers.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.hq.androidtool.config.DevicesState;
import org.hq.androidtool.config.GuiConfig;
import org.hq.androidtool.controllers.DeviceController;
import org.hq.androidtool.models.Device;

import java.util.List;

public class MenuBarController {
    @FXML
    private ComboBox<String> chbxDevices;
    @FXML
    private Label lbl_app_version;
    @FXML
    private HBox pnlFilesPage;
    @FXML
    private HBox pnlMenuPage;
    @FXML
    private HBox pnlContactPage;
    @FXML
    private HBox pnlAppsPage;

    private DeviceController deviceController;
    private GuiMainController guiMainController;
    private List<Device> deviceList;

    public MenuBarController(GuiMainController mainController) {
        this.guiMainController = mainController;
    }
    @FXML
    public void initialize(){
        this.deviceController = new DeviceController();
        reloadDevices();
    }

    public void onMenuButton(MouseEvent event){
        String deviceName = chbxDevices.getValue();
        guiMainController.loadContent(GuiConfig.MENU_PAGE_PATH, getDeviceByName(deviceName));
    }
    public void onContactsPage(MouseEvent event){
        String deviceName = chbxDevices.getValue();
        guiMainController.loadContent(GuiConfig.CONTACTS_PAGE_PATH, getDeviceByName(deviceName));
    }
    public void onAppsPageButton(MouseEvent event){
        String deviceName = chbxDevices.getValue();
        guiMainController.loadContent(GuiConfig.APPS_PAGE_PATH, getDeviceByName(deviceName));
    }
    public void onFilesPageButton(MouseEvent event) {
        String deviceName = chbxDevices.getValue();
        guiMainController.loadContent(GuiConfig.FILES_PAGE_PATH, getDeviceByName(deviceName));
    }

    public void onReloadButton(MouseEvent event){
        reloadDevices();
    }
    private void reloadDevices(){
        this.deviceList = this.deviceController.getDevices();
        chbxDevices.getItems().clear();

        for ( Device device : deviceList ) {
            if ( isDeviceAvailable(device) ) {
                chbxDevices.getItems().add(device.getDeviceName());
            }
        }

        refactorMenu(chbxDevices.getItems().isEmpty());

        if (!chbxDevices.getItems().isEmpty()) {
            chbxDevices.setValue(chbxDevices.getItems().get(0));
        }
    }
    private void  refactorMenu(Boolean state){
        pnlMenuPage.setDisable(state);
        pnlFilesPage.setDisable(state);
        pnlContactPage.setDisable(state);
        pnlAppsPage.setDisable(state);
        pnlAppsPage.setDisable(state);
    }
    private Boolean isDeviceAvailable(Device device){
        return device != null && device.getDeviceState().equals( DevicesState.DEVICE );
    }


    private Device getDeviceByName(String deviceName){
        for (Device device : this.deviceList) {
            if ( device.getDeviceName().equals(deviceName) ) {
                return device;
            }
        }

        return null;
    }
}
