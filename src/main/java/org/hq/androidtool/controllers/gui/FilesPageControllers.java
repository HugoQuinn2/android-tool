package org.hq.androidtool.controllers.gui;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.hq.androidtool.config.FilesType;
import org.hq.androidtool.controllers.FilesController;
import org.hq.androidtool.models.Contact;
import org.hq.androidtool.models.Device;
import org.hq.androidtool.models.File;

import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class FilesPageControllers {
    @FXML
    private TableView<File> tblFiles;
    @FXML
    private HBox pnlHistory;
    @FXML
    private Button btnRootFiles;

    private Device device;
    private FilesController filesController;
    private List<File> files;
    private ObservableList<File> fileObservableList;
    private int idButtonHistory = 0;

    private TableColumn<File, String> nameColumn;
    private TableColumn<File, String> userColumn;
    private TableColumn<File, String> sizeColumn;
    private TableColumn<File, String> dateColumn;
    private TableColumn<File, FontIcon> fileTypeColumn;
    private TableColumn<File, String> pathColumn;

    public FilesPageControllers(Device device) {
        this.device = device;
        this.filesController = new FilesController(this.device);
    }

    @FXML
    public void initialize(){
        files = filesController.getFilesFrom("/");
        createTableFiles();
        fillData();

        btnRootFiles.setId("0,/");
        btnRootFiles.setOnAction(event -> noButtonReturn(btnRootFiles.getId()));
    }

    private void createTableFiles() {
        createColumns();
        createSizesColumns();
        createSpecialEventsTableFiles();
        defineStructureData();
        tblFiles.getColumns().addAll(fileTypeColumn, nameColumn, userColumn, dateColumn, sizeColumn, pathColumn);
    }
    private void createColumns(){
        nameColumn = new TableColumn<>("Archivo");
        userColumn = new TableColumn<>("Usuario");
        sizeColumn = new TableColumn<>("Peso");
        dateColumn = new TableColumn<>("Fecha");
        fileTypeColumn = new TableColumn<>("Tipo");
        pathColumn = new TableColumn<>("Ubicacion");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        fileTypeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFileIcon()));
    }
    private void createSizesColumns(){
        fileTypeColumn.setMaxWidth(60);
        fileTypeColumn.setMinWidth(60);

        userColumn.setMaxWidth(100);
        userColumn.setMinWidth(100);

        dateColumn.setMaxWidth(200);
        dateColumn.setMinWidth(200);

        sizeColumn.setMaxWidth(100);
        sizeColumn.setMinWidth(100);

        pathColumn.setMaxWidth(200);
        pathColumn.setMinWidth(200);
    }
    private void createSpecialEventsTableFiles(){
        fileTypeColumn.setCellFactory(column -> new TableCell<File, FontIcon>() {
            @Override
            protected void updateItem(FontIcon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    FontIcon icon = new FontIcon(item.getIconCode());
                    setGraphic(icon);
                    setText(null);
                }
            }
        });

        tblFiles.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TablePosition<?, ?> pos = tblFiles.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                int column = pos.getColumn();

                File selectedItem = tblFiles.getItems().get(row);
                String selectedColumnName = tblFiles.getColumns().get(column).getText();

                handleCellDoubleClick(selectedItem, selectedColumnName);
            }
        });
    }
    private void defineStructureData(){
        sizeColumn.setCellFactory(column -> new TableCell<File, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {

                        double size = Double.parseDouble(item);
                        if (size < 1024) {
                            setText(String.format("%.0f KB", size));
                        } else if (size < 1024 * 1024) {
                            setText(String.format("%.1f MB", size / 1024));
                        } else if (size < 1024 * 1024 * 1024) {
                            setText(String.format("%.1f GB", size / (1024 * 1024)));
                        } else {
                            setText(String.format("%.0f GB", size / (1024 * 1024 * 1024)));
                        }
                    } catch (NumberFormatException e) {
                        setText("Invalid Size");
                    }
                }
            }
        });
    }
    private void fillData() {
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
    private void handleCellDoubleClick(File item, String columnName) {
        if (item.getFileType() == FilesType.FOLDER) {
            String newPath = item.getPath() + item.getName() + "/";
            List<File> newFiles = filesController.getFilesFrom(newPath);

            if (!newFiles.isEmpty()) {

                Button history = new Button(item.getName());
                Label next = new Label(">");

                idButtonHistory ++;
                history.setId(idButtonHistory + "," + newPath);
                history.getStyleClass().add("no-button");
                history.setOnAction(event -> noButtonReturn(history.getId()));

                pnlHistory.getChildren().addAll(next, history);
                files = newFiles;
                tblFiles.getItems().clear();
                fillData();

                System.out.println("New Button Add:" + history.getId());
            }
        }
    }

    private void noButtonReturn(String idButton){

        String[] idB = idButton.split(",");
        int id = Integer.parseInt(idB[0]);
        String newPath = idB[1];

        idButtonHistory = id;
        int idFromDrop = id + 1;

        System.out.println("Droping Butons from: " + idFromDrop + " to " + pnlHistory.getChildren().size());
        pnlHistory.getChildren().remove(id + idFromDrop, pnlHistory.getChildren().size());

        files = filesController.getFilesFrom(newPath);
        tblFiles.getItems().clear();
        fillData();
    }
    public void onButtonDrop() {

    }
    public void onButtonExport() {
        File file = tblFiles.getSelectionModel().getSelectedItem();
        if (file.getFileType() == FilesType.FOLDER) {

        } else {

        }
    }
    public void onButtonNewFolder() {

    }
    public void onButtonSend() {

    }
}
