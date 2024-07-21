package org.hq.androidtool.controllers.gui;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import org.hq.androidtool.controllers.ContactsControllers;
import org.hq.androidtool.models.Contact;
import org.hq.androidtool.models.Device;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.ArrayList;
import java.util.List;

public class ContactsPageController {
    private Device device;
    private ContactsControllers contactsControllers;
    private List<Contact> contacts = new ArrayList<>();
    private ObservableList<Contact> contactObservableList;

    private TableColumn<Contact, Integer> idColumn;
    private TableColumn<Contact, String> nameColumn;
    private TableColumn<Contact, String> emailColumn;
    private TableColumn<Contact, String> phoneColumn;

    @FXML
    private TableView<Contact> tblContacts;
    @FXML
    private TextField txtFilterContacts;

    public ContactsPageController (Device device){
        this.device = device;

    }

    @FXML
    public void initialize(){
        this.contactsControllers = new ContactsControllers();

        idColumn = new TableColumn<>("ID");
        nameColumn = new TableColumn<>("Nombre");
        emailColumn = new TableColumn<>("Email");
        phoneColumn = new TableColumn<>("Phone");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        idColumn.setMaxWidth(50);
        idColumn.setMinWidth(50);

        nameColumn.setResizable(false);
        emailColumn.setResizable(false);
        phoneColumn.setResizable(false);

        tblContacts.getColumns().addAll(idColumn, nameColumn, phoneColumn, emailColumn);

        tblContacts.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            double idColumnWidth = 60;
            double availableWidth = totalWidth - idColumnWidth;

            double remainingColumnWidth = availableWidth / (tblContacts.getColumns().size() - 1);

            for (TableColumn<Contact, ?> column : tblContacts.getColumns()) {
                if (column.equals(idColumn)) {
                    column.setPrefWidth(idColumnWidth);
                } else {
                    column.setPrefWidth(remainingColumnWidth);
                }
            }
        });

        fillData();
        initFilter();
    }

    public void fillData() {
        Task<List<Contact>> task = new Task<>() {
            @Override
            protected List<Contact> call() throws Exception {
                contacts = contactsControllers.getContacts(device);
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

    private void initFilter() {
        txtFilterContacts.setPromptText("Buscar contactos");
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

}
