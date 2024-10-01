package org.hq.androidtool.contacts;


import org.hq.androidtool.adb.CommandController;
import org.hq.androidtool.device.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ContactsControllers {
    private CommandController commandController;
    private static final Logger logger = LoggerFactory.getLogger(ContactsControllers.class);

    public ContactsControllers(){
        this.commandController = new CommandController();
    }

    public List<Contact> getContacts(Device device){
        logger.info("Descargando contactos de " + device.getDeviceName());

        List<String> listContacts = commandController.getContacts(device);
        List<Contact> Contacts = new ArrayList<>();

        for (String con : listContacts){
            String[] data = con.split(",");
            Contacts.add(
                    Contact
                            .builder()
                            .id(Integer.parseInt(data[0]))
                            .name(data[1])
                            .phone(data[2])
                            .email(data[3])
                            .build()
            );
        }

        return Contacts;
    }

    public void callContact(Device device, Contact contact){
        commandController.callPhone(device, contact.getPhone());
    }

    public void callPhone(Device device, String number){
        commandController.callPhone(device, number);
    }

    public boolean exportToCSV(List<Contact> contacts, String csvPath) {
        logger.info("Descargando " + contacts.size() + " contactos a " + csvPath);

        File file = new File(csvPath);

        try (PrintWriter writer = new PrintWriter(new PrintWriter(file))) {
            writer.println("ID, name, phone, email");

            for (Contact contact : contacts) {
                writer.printf("%d,%s,%s,%s%n", contact.getId(), contact.getName(), contact.getPhone(), contact.getEmail());
            }

            return true;

        } catch (IOException e) {
            logger.error("Error en exportacion: " + e.getMessage());
            return false;
        }
    }


}
