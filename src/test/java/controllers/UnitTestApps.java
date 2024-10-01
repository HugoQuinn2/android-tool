package controllers;

import org.hq.androidtool.apps.AppsController;
import org.hq.androidtool.adb.CommandController;
import org.hq.androidtool.device.DeviceController;
import org.hq.androidtool.device.Device;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UnitTestApps {
    private CommandController commandController;
    private DeviceController deviceController;
    private AppsController appsController;

    @Test
    void getPackages(){
        commandController = new CommandController();
        deviceController = new DeviceController();

        List<Device> devices = deviceController.getDevices();
        List<String> packs = commandController.getPackages(devices.get(0));

        appsController = new AppsController(devices.get(0));


        System.out.println(appsController.getApplications());
    }
}
