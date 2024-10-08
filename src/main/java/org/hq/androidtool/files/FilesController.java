package org.hq.androidtool.files;

import javafx.concurrent.Task;
import org.hq.androidtool.constants.FilesType;
import org.hq.androidtool.adb.CommandController;
import org.hq.androidtool.device.Device;
import org.hq.androidtool.utils.AdbCommandFiles;

import java.util.ArrayList;
import java.util.List;

public class FilesController {
    private CommandController commandController;
    private AdbCommandFiles adbCommandFiles;
    private Device device;
    private Task pull;

    public FilesController(Device device) {
        commandController = new CommandController();
        adbCommandFiles = new AdbCommandFiles();
        this.device = device;
    }

    public List<FileDevice> getFilesFrom(String path) {
        List<FileDevice> fileDevices = new ArrayList<>();
        List<String> filesFormat = commandController.ls(device, path);


        if (filesFormat != null) {
            for (String fileFormat : filesFormat) {
                String[] data = fileFormat.split(",");

                fileDevices.add(
                        FileDevice
                                .builder()
                                .fileType(getFileType(data[0]))
                                .user(data[1])
                                .size(data[2])
                                .date(data[3].substring(0,16))
                                .name(data[4])
                                .path(path)
                                .build()
                );
            }

            return fileDevices;
        }

        return null;
    }

    private FilesType getFileType(String format) {
        if (format.contains("FILE")) {
            return FilesType.FILE;
        } else if (format.contains("FOLDER")) {
            return FilesType.FOLDER;
        } else if (format.contains("SYMBOLIC_LINK")) {
            return FilesType.SYMBOLIC_LINK;
        }

        return FilesType.INDETERMINATE;
    }

    public boolean mkdir(String nameFolder, String path) {
        return commandController.mkdir(device, nameFolder, path);
    }

    public boolean pull(FileDevice fileDevice, String to){
        String filePath = fileDevice.getPath() +  fileDevice.getName();
        return adbCommandFiles.pull(device, filePath, to);
    }

//    public boolean push(String from, String to) {
//
//    }

}
