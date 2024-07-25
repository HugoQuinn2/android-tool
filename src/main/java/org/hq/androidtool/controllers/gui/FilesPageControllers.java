package org.hq.androidtool.controllers.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hq.androidtool.controllers.FilesController;
import org.hq.androidtool.models.Contact;
import org.hq.androidtool.models.Device;
import org.hq.androidtool.models.File;

import javafx.scene.control.TableView;
import java.util.Date;
import java.util.List;

public class FilesPageControllers {
    @FXML
    private TableView<File> tblFiles;

    private Device device;
    private FilesController filesController;
    private List<File> files;
    private ObservableList<File> fileObservableList;

    private TableColumn<File, String> nameColumn;
    private TableColumn<File, String> userColumn;
    private TableColumn<File, String> sizeColumn;
    private TableColumn<File, Date> dateColumn;
    private TableColumn<File, String> fileTypeColumn;

    public FilesPageControllers(Device device) {
        this.device = device;
        this.filesController = new FilesController(this.device);
    }

    @FXML
    public void initialize(){
        files = filesController.getFilesFrom("/");
        createTableFiles();
        fillData();
    }

    private void createTableFiles() {
        nameColumn = new TableColumn<>("Archivo");
        userColumn = new TableColumn<>("Usuario");
        sizeColumn = new TableColumn<>("Peso");
        dateColumn = new TableColumn<>("Fecha");
        fileTypeColumn = new TableColumn<>("Tipo");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        fileTypeColumn.setCellValueFactory(new PropertyValueFactory<>("fileType"));

        tblFiles.getColumns().addAll(nameColumn, userColumn, sizeColumn, dateColumn, fileTypeColumn);
    }

    public void fillData() {
        Task<List<Contact>> task = new Task<>() {
            @Override
            protected List<Contact> call() throws Exception {
                fileObservableList = FXCollections.observableArrayList(files);
                tblFiles.setItems( fileObservableList );
                return null;
            }
        };

        task.setOnFailed(e -> {
            Platform.runLater(() -> {
                System.err.println("No se pudo cargar los datos del dispositivo");
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
