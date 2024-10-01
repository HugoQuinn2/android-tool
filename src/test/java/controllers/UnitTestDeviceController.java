package controllers;

import org.hq.androidtool.device.DeviceController;
import org.hq.androidtool.device.Device;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UnitTestDeviceController {
    List<Device> devices = new ArrayList<>();
    @Test
    void detDevicesData(){
        DeviceController deviceController = new DeviceController();
        this.devices = deviceController.getDevices();

        for (Device device : devices){
            System.out.println(deviceController.getDeviceData(device));
        }
    }
}
