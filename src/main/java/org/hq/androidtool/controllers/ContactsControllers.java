package org.hq.androidtool.controllers;


import org.hq.androidtool.models.Contact;
import org.hq.androidtool.models.Device;

import java.util.ArrayList;
import java.util.List;

public class ContactsControllers {
    private CommandController commandController;

    public ContactsControllers(){
        this.commandController = new CommandController();
    }

    public List<Contact> getContacts(Device device){
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


}
