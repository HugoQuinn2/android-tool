package org.hq.androidtool.controllers;

import org.hq.androidtool.adb.*;
import org.hq.androidtool.models.AndroidDeviceModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AndroidDeviceController {
    private final controller adbController;
    private AndroidDeviceModel androidDeviceModel;
    private final int intervalThread = 1; //Segundos

    public AndroidDeviceController(){
        this.adbController = new controller();
        androidDeviceModel = fetchData();
    }
    public AndroidDeviceModel fetchData(){
        adbController.device();

        return AndroidDeviceModel
                .builder()
                .deviceName(adbController.getDeviceName())
                .deviceModel(adbController.getDeviceModel())
                .stateDevice(adbController.getStateDevice())
                .serialNumber(adbController.getSerialNumber())
                .macDevice(adbController.getMacDevice())
                .ipDevice(adbController.getIpDevice())
                .applicationDevice(adbController.getApplicationsDevice())
                .fileInfoModelList(adbController.getFileInfoDevice("/"))
                .androidVersion(adbController.getAndroidVersion())
                .simContract(adbController.getSimContract())
                .manufacturer(adbController.getManufacturer())
                .storage(adbController.getStorage())
                .build();
    }
    public String getModelDevice(){
        return androidDeviceModel.getDeviceModel();
    }
    public String getSerialDevice(){
        return androidDeviceModel.getSerialNumber();
    }
    public String getMAC(){
        return androidDeviceModel.getMacDevice();
    }
    public String getIP(){
        return androidDeviceModel.getIpDevice();
    }
    public List<String> getAplications(){
        return androidDeviceModel.getApplicationDevice();
    }
}
