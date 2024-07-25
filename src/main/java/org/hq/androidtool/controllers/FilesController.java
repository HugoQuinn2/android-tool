package org.hq.androidtool.controllers;

import org.hq.androidtool.config.FilesType;
import org.hq.androidtool.models.Device;
import org.hq.androidtool.models.File;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilesController {
    private CommandController commandController;
    private Device device;

    public FilesController(Device device) {
        commandController = new CommandController();
        this.device = device;
    }

    public List<File> getFilesFrom(String path) {
        List<File> files = new ArrayList<>();
        List<String> filesFormat = commandController.ls(device, path);
        String formato = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(formato);

        if (!filesFormat.isEmpty()) {
            try {
                for (String fileFormat : filesFormat) {
                    String[] data = fileFormat.split(",");
                    files.add(
                            File
                                    .builder()
                                    .fileType(getFileType(data[0]))
                                    .user(data[1])
                                    .size(data[2])
                                    .date(isValidDate(data[3], sdf) ? sdf.parse(data[3]) : null )
                                    .name(data[4])
                                    .build()
                    );
                }

                return files;
            } catch (ParseException e) {
                System.err.println(e.getMessage());
                return null;
            }
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

    private static boolean isValidDate(String date, SimpleDateFormat sdf) {
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
