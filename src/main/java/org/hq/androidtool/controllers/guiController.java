package org.hq.androidtool.controllers;

import org.hq.androidtool.models.AndroidDeviceModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class guiController {
    @FXML
    private Label lblDevice;
    @FXML
    private Label lblModelDevice;
    @FXML
    private Label lblSerialN;
    @FXML
    private Label lblIP;
    @FXML
    private Label lblMac;

    public void updateData(AndroidDeviceModel androidDeviceModel){
        System.out.println(androidDeviceModel.toString());
    }


}
