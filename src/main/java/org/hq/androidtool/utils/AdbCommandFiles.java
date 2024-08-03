package org.hq.androidtool.utils;

import javafx.concurrent.Task;
import org.hq.androidtool.controllers.CommandController;
import org.hq.androidtool.models.Device;

public class AdbCommandFiles {
    CommandController commandController;
    private Task pullTask;
    private Task pushTask;

    public AdbCommandFiles() {
        commandController = new CommandController();
    }

    public boolean pull(Device device, String from, String to){
        String output = commandController.pull(device, from, to);
        return output.contains("1 file pulled");
    }

//    private Task getPullTask() {
//
//    }
}
