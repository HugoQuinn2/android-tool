package org.hq.androidtool.controllers.gui;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.hq.androidtool.constants.FilesType;
import org.hq.androidtool.controllers.FilesController;
import org.hq.androidtool.models.Contact;
import org.hq.androidtool.models.Device;
import org.hq.androidtool.models.FileDevice;

import org.hq.androidtool.services.PullTaskService;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.util.List;

public class FilesPageControllers {
    @FXML
    private TableView<FileDevice> tblFiles;
    @FXML
    private HBox pnlHistory;
    @FXML
    private Button btnRootFiles;
    @FXML
    private TextField txtFilter;

    private Device device;
    private FilesController filesController;
    private List<FileDevice> fileDevices;
    private ObservableList<FileDevice> fileDeviceObservableList;
    private int idButtonHistory = 0;
    private static final Logger logger = LoggerFactory.getLogger(FilesPageControllers.class);

    private TableColumn<FileDevice, String> nameColumn;
    private TableColumn<FileDevice, String> userColumn;
    private TableColumn<FileDevice, String> sizeColumn;
    private TableColumn<FileDevice, String> dateColumn;
    private TableColumn<FileDevice, FontIcon> fileTypeColumn;
    private TableColumn<FileDevice, String> pathColumn;

    public FilesPageControllers(Device device) {
        this.device = device;
        this.filesController = new FilesController(this.device);
    }

    @FXML
    public void initialize(){
        fileDevices = filesController.getFilesFrom("/");

        createTableFiles();
        fillData();
        initFilter();

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

        dateColumn.setMaxWidth(150);
        dateColumn.setMinWidth(150);

        sizeColumn.setMaxWidth(100);
        sizeColumn.setMinWidth(100);

        pathColumn.setMaxWidth(250);
        pathColumn.setMinWidth(250);
    }
    private void createSpecialEventsTableFiles(){
        // Define icon file per row
        fileTypeColumn.setCellFactory(column -> new TableCell<FileDevice, FontIcon>() {
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

        // Double-click on row for browse public folders
        tblFiles.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TablePosition<?, ?> pos = tblFiles.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                int column = pos.getColumn();

                FileDevice selectedItem = tblFiles.getItems().get(row);
                String selectedColumnName = tblFiles.getColumns().get(column).getText();

                handleCellDoubleClick(selectedItem, selectedColumnName);
            }
        });

        // KeyEvent Enter on row for browse public folders
        tblFiles.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                TablePosition<?, ?> pos = tblFiles.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                int column = pos.getColumn();

                FileDevice selectedItem = tblFiles.getItems().get(row);
                String selectedColumnName = tblFiles.getColumns().get(column).getText();

                handleCellDoubleClick(selectedItem, selectedColumnName);
            }
        });
    }
    private void defineStructureData(){
        sizeColumn.setCellFactory(column -> new TableCell<FileDevice, String>() {
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
                fileDeviceObservableList = FXCollections.observableArrayList(fileDevices);
                tblFiles.setItems(fileDeviceObservableList);
                return null;
            }
        };

        task.setOnFailed(e -> {
            Platform.runLater(() -> {
                logger.error("No se pudo cargar los contactos: " + e);
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }
    private void handleCellDoubleClick(FileDevice item, String columnName) {
        if (item.getFileType() == FilesType.FOLDER) {
            String newPath = (item.getPath() + item.getName() + "/").replaceAll(" ", "");
            List<FileDevice> newFileDevices = filesController.getFilesFrom(newPath);

            Button history = new Button(item.getName());
            Label next = new Label("/");

            idButtonHistory++;

            next.getStyleClass().add("important-text");

            history.setId(idButtonHistory + "," + newPath);
            history.getStyleClass().add("link-button");
            history.setOnAction(event -> noButtonReturn(history.getId()));

            pnlHistory.getChildren().addAll(next, history);
            fileDevices = newFileDevices;
            tblFiles.getItems().clear();
            fillData();

            System.out.println("New Button Add:" + history.getId());
        }
    }
    private void initFilter() {
        txtFilter.setPromptText("Buscar...");
        txtFilter.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String filterText = txtFilter.textProperty().get().toLowerCase();

                if (filterText.isEmpty()){
                    tblFiles.setItems(fileDeviceObservableList);
                    return;
                }

                ObservableList<FileDevice> newFileDeviceObservableList = FXCollections.observableArrayList();
                ObservableList<TableColumn<FileDevice, ?>> cols = tblFiles.getColumns();

                for (FileDevice fileDevice : fileDevices){
                    for (TableColumn<FileDevice, ?> column : cols){
                        String cellValue = column.getCellData(fileDevice).toString();
                        if (!cellValue.isEmpty() && cellValue.toLowerCase().contains(filterText)) {
                            newFileDeviceObservableList.add(fileDevice);
                        }
                    }
                }

                tblFiles.setItems(newFileDeviceObservableList);
            }
        });
    }

    private void noButtonReturn(String idButton){

        String[] idB = idButton.split(",");
        int id = Integer.parseInt(idB[0]);
        String newPath = idB[1];

        idButtonHistory = id;
        int idFromDrop = id + 1;

        System.out.println("Droping Butons from: " + idFromDrop + " to " + pnlHistory.getChildren().size());
        pnlHistory.getChildren().remove(id + idFromDrop, pnlHistory.getChildren().size());

        fileDevices = filesController.getFilesFrom(newPath);
        tblFiles.getItems().clear();
        fillData();
    }
    public void onButtonDrop() {

    }
    public void onButtonExport() {
        FileDevice fileDevice = tblFiles.getSelectionModel().getSelectedItem();

        if (fileDevice != null) {
            File file = (new DirectoryChooser()).showDialog(new Stage());
            if (file != null) {
                PullTaskService pullTaskService = new PullTaskService(device, fileDevice, file.getAbsolutePath());
                pullTaskService.start();
            }
        }

    }
    public void onButtonNewFolder() {

    }
    public void onButtonSend() {

    }

}
