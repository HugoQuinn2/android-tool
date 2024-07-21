package controllers;

import org.hq.androidtool.controllers.CommandController;
import org.hq.androidtool.controllers.DeviceController;
import org.hq.androidtool.models.Device;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UnitTestApps {
    private CommandController commandController;
    private DeviceController deviceController;

    @Test
    void getPackages(){
        commandController = new CommandController();
        deviceController = new DeviceController();

        List<Device> devices = deviceController.getDevices();
        List<String> packs = commandController.getPackages(devices.get(0));

        System.out.println(commandController.getPackageInfo(devices.get(0), packs.get(2)));
    }
}
