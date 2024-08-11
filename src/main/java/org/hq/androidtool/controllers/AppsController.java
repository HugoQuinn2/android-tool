package org.hq.androidtool.controllers;

import org.hq.androidtool.models.Application;
import org.hq.androidtool.models.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AppsController {
    private CommandController commandController = new CommandController();
    private Device device;
    private static final Logger logger = LoggerFactory.getLogger(AppsController.class);

    public AppsController(Device device){
        commandController = new CommandController();
        this.device = device;
    }

    public List<Application> getApplications(){
        List<Application> applications = new ArrayList<>();
        List<String> packList = commandController.getPackages(device);

        for (String pack : packList) {
            applications.add(
                    Application
                            .builder()
                            .packageName(pack)
                            .name(extractNamePackage(pack))
                            .path(null)
                            .size(null)
                            .build()
            );
        }

        return  applications;
    }

    public Boolean dropApp(String pack) {
        return commandController.dropApp(device, pack).contains("Success");
    }

    public String getBasePathApp(String pack) {
        return commandController.getPathApp(device, pack);
    }

    public boolean pull(String from, String to){
        logger.info( device.getDeviceName() + " | " + "Exportando " + from);
        String output = commandController.pull(device, from, to);
        return output.contains("1 file pulled");
    }

    public boolean pullBaseApk(String pack, String to){
        String basePath = getBasePathApp(pack);
        return pull(basePath, to);
    }

    public boolean installApk(String apkPath) {
        String output = commandController.install(device,apkPath);
        return output.contains("Performing Streamed Install");
    }

    private String extractNamePackage(String pack){
        List<String> packStructure = List.of(pack.split("\\."));
        return packStructure.get(packStructure.size() - 1).trim();
    }
}
