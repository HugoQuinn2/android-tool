package org.hq.androidtool.controllers.gui;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.hq.androidtool.controllers.ContactsControllers;
import org.hq.androidtool.models.Contact;
import org.hq.androidtool.models.Device;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactsPageController {

    @FXML
    private TableView<Contact> tblContacts;
    @FXML
    private TextField txtFilterContacts;
    @FXML
    private Button btnCallContact;
    @FXML
    private Button btnDropContact;
    @FXML
    private Button btnExportContacts;

    private Device device;
    private ContactsControllers contactsControllers;
    private List<Contact> contacts = new ArrayList<>();
    private ObservableList<Contact> contactObservableList;

    private TableColumn<Contact, Integer> idColumn;
    private TableColumn<Contact, String> nameColumn;
    private TableColumn<Contact, String> emailColumn;
    private TableColumn<Contact, String> phoneColumn;
    private final static int rowsPerPage = 20;

    public ContactsPageController (Device device){
        this.device = device;
    }

    @FXML
    public void initialize(){
        this.contactsControllers = new ContactsControllers();
        contacts = contactsControllers.getContacts(device);

        createTableContacts();
        createListenerSizeTableContacts();
        fillData();
        initFilter();
    }

    private void createTableContacts() {
        idColumn = new TableColumn<>("ID");
        nameColumn = new TableColumn<>("Nombre");
        emailColumn = new TableColumn<>("Email");
        phoneColumn = new TableColumn<>("Phone");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        //idColumn.setMaxWidth(50);
        //idColumn.setMinWidth(50);

        nameColumn.setResizable(false);
        emailColumn.setResizable(false);
        phoneColumn.setResizable(false);

        tblContacts.getColumns().addAll(idColumn, nameColumn, phoneColumn, emailColumn);
    }

    private void fillData() {
        Task<List<Contact>> task = new Task<>() {
            @Override
            protected List<Contact> call() throws Exception {
                contactObservableList = FXCollections.observableArrayList(contacts);
                tblContacts.setItems( contactObservableList );
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

    private void createListenerSizeTableContacts() {
        tblContacts.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            double idColumnWidth = 0;
            double availableWidth = totalWidth - idColumnWidth;

            double remainingColumnWidth = availableWidth / (tblContacts.getColumns().size());

            for (TableColumn<Contact, ?> column : tblContacts.getColumns()) {
                column.setPrefWidth(remainingColumnWidth);
            }
        });
    }

    private void initFilter() {
        txtFilterContacts.setPromptText("Buscar...");
        txtFilterContacts.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String filterText = txtFilterContacts.textProperty().get().toLowerCase();

                if (filterText.isEmpty()){
                    tblContacts.setItems(contactObservableList);
                    return;
                }

                ObservableList<Contact> newContactObservableList = FXCollections.observableArrayList();
                ObservableList<TableColumn<Contact, ?>> cols = tblContacts.getColumns();

                for (Contact contact : contacts){
                    for (TableColumn<Contact, ?> column : cols){
                        String cellValue = column.getCellData(contact).toString();
                        if (!cellValue.isEmpty() && cellValue.toLowerCase().contains(filterText)) {
                            newContactObservableList.add(contact);
                        }
                    }
                }

                tblContacts.setItems(newContactObservableList);
            }
        });
    }

    public void onBtnDropContact(MouseEvent event) {

    }

    public void onBtnCallContact(MouseEvent event) {
        Contact selectedContact = tblContacts.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            contactsControllers.callPhone(device, selectedContact.getPhone());
        }
    }

    public void onBtnExportContacts(MouseEvent event) {
        if (tblContacts.getItems() != null) {
            File file = (new DirectoryChooser()).showDialog(new Stage());
            if (file != null) {
                if (contactsControllers.exportToCSV(contacts, file.getAbsolutePath() + File.separator + "contacts.csv")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exportacion de contactos");
                    alert.setHeaderText(null);
                    alert.setContentText("Exportacion Exitosa");
                    alert.showAndWait();

                    return;
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exportacion de contactos");
        alert.setHeaderText(null);
        alert.setContentText("No se pudo exportar los contactos");
        alert.showAndWait();
    }

}
