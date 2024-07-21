package org.hq.androidtool.controllers;

import org.hq.androidtool.models.Application;
import org.hq.androidtool.models.Device;

import java.util.ArrayList;
import java.util.List;

public class AppsController {
    private CommandController commandController = new CommandController();

    public AppsController(){
        commandController = new CommandController();
    }

    public List<Application> getApplications(Device device){
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

    private String extractNamePackage(String pack){
        List<String> packStructure = List.of(pack.split("\\."));
        return packStructure.get(packStructure.size() - 1).trim();
    }
}
