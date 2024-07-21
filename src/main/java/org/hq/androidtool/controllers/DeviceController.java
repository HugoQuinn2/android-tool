package org.hq.androidtool.controllers;

import org.hq.androidtool.config.DevicesState;
import org.hq.androidtool.models.Device;
import org.hq.androidtool.models.DeviceData;

import java.util.ArrayList;
import java.util.List;

public class DeviceController {
    private final CommandController commandController;

    public DeviceController(){
        this.commandController = new CommandController();
    }

    public List<Device> getDevices(){
        List<Device> devices = new ArrayList<>();
        List<String> ListDevices = commandController.getDevices();

        for (String dev : ListDevices) {
            String[] parts = dev.split(",");
            if (parts.length == 2) {
                String deviceName = parts[0].trim();
                String deviceState = parts[1].trim();

                if ( deviceState.equals("DEVICE") ) {
                    devices.add(
                            Device.builder()
                                    .deviceName(deviceName)
                                    .deviceState(DevicesState.DEVICE)
                                    .build()
                    );
                } else if ( deviceState.equals("UNAUTHORIZED") ) {
                    devices.add(
                            Device.builder()
                                    .deviceName(deviceName)
                                    .deviceState(DevicesState.UNAUTHORIZED)
                                    .build()
                    );
                } else if ( deviceState.equals("NO_DEVICE") ) {
                    devices.add(
                            Device.builder()
                                    .deviceName(deviceName)
                                    .deviceState(DevicesState.NO_DEVICE)
                                    .build()
                    );
                }

            }
        }

        return devices;
    }

    public DeviceData getDeviceData( Device device ){
        return DeviceData
                .builder()
                .deviceModel    (commandController.getModel( device ) )
                .macDevice      (commandController.getMacDevice( device ) )
                .simContract    (commandController.getSimContract( device ) )
                .ipDevice       (commandController.getIpDevice( device ) )
                .manufacturer   (commandController.getManufacturer( device ) )
                .serialNumber   (commandController.getSerialNumber( device ) )
                .androidVersion (commandController.getAndroidVersion( device ) )
                .storage        (commandController.getStorage( device ) )
                .ram            (commandController.getRam(device))
                .displaySize    (commandController.getDisplay(device))
                .androidId      (commandController.getAndroidId(device))
                .processor      (null)
                .build();
    }
}
