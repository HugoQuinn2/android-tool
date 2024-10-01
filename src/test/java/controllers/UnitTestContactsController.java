package controllers;

import org.hq.androidtool.contacts.ContactsControllers;
import org.hq.androidtool.device.DeviceController;
import org.hq.androidtool.contacts.Contact;
import org.hq.androidtool.device.Device;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UnitTestContactsController {
    List<Device> devices = new ArrayList<>();
    List<Contact> contacts = new ArrayList<>();
    ContactsControllers contactsControllers;
    DeviceController deviceController;

    @Test
    void ContactsController(){
        this.contactsControllers = new ContactsControllers();
        this.deviceController = new DeviceController();

        this.devices = deviceController.getDevices();

        this.contacts = contactsControllers.getContacts( devices.get(0) );

        System.out.println(this.devices);
        System.out.println(this.contacts);
    }

    @Test
    void CallPhone(){
        this.contactsControllers = new ContactsControllers();
        this.deviceController = new DeviceController();

        this.devices = deviceController.getDevices();

        String number = "+521354105";
        Device device = devices.get(0);
        System.out.println( String.format("Calling to %s in %s", number, device.getDeviceName()) );
        contactsControllers.callPhone(device,number);
    }
}
