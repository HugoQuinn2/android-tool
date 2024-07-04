package org.hq.androidtool.gui;

import org.hq.androidtool.models.AndroidDeviceModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class popup_info {

    @FXML
    private TextField lblSerialNumber;
    @FXML
    private TextField lblModel;
    @FXML
    private TextField lblMAC;
    @FXML
    private TextField lblIP;
    @FXML
    private TextField lblAndroidVersion;
    @FXML
    private TextField lblContract;
    @FXML
    private TextField lblManufacturer;
    @FXML
    private TextField lblStorage;
    @FXML
    private Label lblPerStorage;
    @FXML
    private ProgressBar progres_storeage;

    private String serialnumber;
    private String model;
    private String mac;
    private String ip;
    private String androidVersion;
    private AndroidDeviceModel androidDeviceModel;

    public popup_info(AndroidDeviceModel androidDeviceModel){
        this.androidDeviceModel = androidDeviceModel;

        this.serialnumber = androidDeviceModel.getSerialNumber();
        this.model = androidDeviceModel.getDeviceModel();
        this.mac = androidDeviceModel.getMacDevice();
        this.ip = androidDeviceModel.getIpDevice();
        this.androidVersion = androidDeviceModel.getAndroidVersion();
    }

    @FXML
    public void initialize(){
        blockTextfield();
        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(false);
        List<String> perceStorage = List.of(androidDeviceModel.getStorage().split("/"));


        double storage = Double.parseDouble(perceStorage.get(0));
        double used = Double.parseDouble(perceStorage.get(1));
        double perce = Double.parseDouble(df.format( (used / storage) * 100 ));


        lblSerialNumber.setText(serialnumber);
        lblIP.setText(ip);
        lblMAC.setText(mac.toUpperCase());
        lblModel.setText(model);
        lblAndroidVersion.setText(androidVersion);
        lblContract.setText(androidDeviceModel.getSimContract());
        lblManufacturer.setText(androidDeviceModel.getManufacturer());
        lblStorage.setText(used + "/" + storage + " " + "GB");
        lblPerStorage.setText( perce + " % de uso" );
        progres_storeage.setProgress(perce / 100);

    }

    private List<TextField> getAllTextField(){
        List<TextField> textFieldList = new ArrayList<>();
        textFieldList.add(lblSerialNumber);
        textFieldList.add(lblIP);
        textFieldList.add(lblMAC);
        textFieldList.add(lblModel);
        textFieldList.add(lblAndroidVersion);
        textFieldList.add(lblContract);
        textFieldList.add(lblManufacturer);
        textFieldList.add(lblStorage);
        return textFieldList;
    }

    public void blockTextfield(){
        List<TextField> textFieldList = getAllTextField();
        for (TextField textField : textFieldList){
            textField.setEditable(false);
            textField.setFocusTraversable(false);
        }
    }



}
