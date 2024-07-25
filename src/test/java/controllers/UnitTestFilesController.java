package controllers;

import org.hq.androidtool.controllers.CommandController;
import org.hq.androidtool.controllers.DeviceController;
import org.hq.androidtool.controllers.FilesController;
import org.hq.androidtool.models.Device;
import org.hq.androidtool.models.File;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UnitTestFilesController {
    @Test
    public void extractData() {
        DeviceController deviceController = new DeviceController();
        CommandController commandController = new CommandController();
        List<Device> devices = deviceController.getDevices();

        System.out.println( commandController.ls(devices.get(0), "/") );
    }

    @Test
    public void makeFilesModel() {
        DeviceController deviceController = new DeviceController();
        List<Device> devices = deviceController.getDevices();
        FilesController filesController = new FilesController(devices.get(0));
        List<File> files = filesController.getFilesFrom("/");


        System.out.println( files );
    }
}
