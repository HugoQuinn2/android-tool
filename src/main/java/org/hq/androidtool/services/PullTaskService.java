package org.hq.androidtool.services;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.controlsfx.dialog.ProgressDialog;
import org.hq.androidtool.config.FilesType;
import org.hq.androidtool.controllers.FilesController;
import org.hq.androidtool.models.Device;
import org.hq.androidtool.models.FileDevice;
import org.hq.androidtool.utils.AdbCommandFiles;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class PullTaskService extends Task<Void> {
    private final FileDevice fileDevice;
    private final String to;
    private AdbCommandFiles adbCommandFiles;
    private Device device;
    private ProgressDialog progressDialog;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private List<FileDevice> noFilesPull;
    private List<FileDevice> filesFolders;

    public PullTaskService(Device device, FileDevice fileDevice, String to) {
        this.fileDevice = fileDevice;
        this.to = to;
        this.device = device;

        adbCommandFiles = new AdbCommandFiles();
        noFilesPull = new ArrayList<>();
        filesFolders = new ArrayList<>();
    }

    public void start() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setContentText( String.format("Exportando %s", fileDevice.getPath() + fileDevice.getName()) );
        progressDialog.setTitle("Exportando archivo(s)");
        progressDialog.setHeaderText("Exportando");
        new Thread(this).start();
        progressDialog.show();
    }

    private void showDialogFinish() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);


        String title = "Resumen de exportacion";
        int totalFiles = filesFolders.isEmpty() ? 1 : filesFolders.size();
        int totalFilesNoExporter = noFilesPull.size();
        String message = noFilesPull.isEmpty() ? String.format("%s exportado correctamente",fileDevice.getPath() + fileDevice.getName()) : String.format("%s/%s no exportados", totalFilesNoExporter, totalFiles);


        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private boolean createFolder(String path, String name) {
        File folder = new File(path + File.separator + name);
        return folder.mkdirs();
    }

    @Override
    protected Void call() throws Exception {
        try {
            if (fileDevice.getFileType() == FilesType.FILE) {
                String file = fileDevice.getPath() + fileDevice.getName();

                if (adbCommandFiles.pull(device, file, to)) {
                    updateProgress(1, 1);
                } else {
                    noFilesPull.add(fileDevice);
                }

            }
            if (fileDevice.getFileType() == FilesType.FOLDER) {
                String masterPath = fileDevice.getPath() + fileDevice.getName();
                filesFolders = new FilesController(device).getFilesFrom(masterPath);
                LocalDateTime now = LocalDateTime.now();
                String masterFolder = fileDevice.getName() + now.format(formatter);
                int i = 1;

                if (createFolder(to, masterFolder)) {
                    String newTo = to + File.separator + masterFolder;

                    for (FileDevice fileDevice : filesFolders) {
                        String file = masterPath + "/" + fileDevice.getName();
                        String message = String.format("Exportando %s\n%s/%s", fileDevice.getName(), i, filesFolders.size());

                        System.out.println(message);

                        if (adbCommandFiles.pull(device, file, newTo)) {
                            updateProgress(i, filesFolders.size());
                        } else {
                            noFilesPull.add(fileDevice);
                        }

                        i++;
                    }
                }
            }
        } finally {
            Platform.runLater(() -> {
                if (progressDialog != null) {
                    progressDialog.close();
                }
                showDialogFinish();
            });
        }
        return null;
    }
}
