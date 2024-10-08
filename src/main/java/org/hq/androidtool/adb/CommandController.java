package org.hq.androidtool.adb;

import javafx.scene.control.Alert;
import org.hq.androidtool.constants.AdbCommands;
import org.hq.androidtool.device.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandController {
    private final AdbCommandBuilder adbCommandBuilder;
    private final AdbService adbService;
    private final AdbParsers adbParsers;
    private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

    public CommandController() {
        this.adbCommandBuilder = new AdbCommandBuilder();
        this.adbService = new AdbService();
        this.adbParsers = new AdbParsers();
    }

    public List<String> getDevices() {
        List<Device> devices = new ArrayList<>();
        List<String> command = adbCommandBuilder.buildCommand(AdbCommands.ADB_DEVICE.getCommand());

        String message = adbService.executeCommand(command);
        return adbParsers.parseOutputDevice(message);
    }

    public String getIpDevice( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_WLAN0.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputIp( output );
    }

    public String getMacDevice( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_WLAN0.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputMac( output ).toUpperCase();
    }

    public String getSerialNumber( Device device ){
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_SERIAL.getCommand( device.getDeviceName() ) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputSerial( output ).toUpperCase();
    }

    public String getModel( Device device ){
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_MODEL.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputModel( output ).toUpperCase();
    }

    public String getManufacturer( Device device ){
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_MANUFACTURER.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputManufacturer( output ).toUpperCase();
    }

    public String getAndroidVersion( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_ANDROID_VERSION.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputAndroidVersion( output ).toUpperCase();
    }

    public String getSimContract( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_SIM_CONTRACT.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputSimContract( output ).toUpperCase();
    }

    public String getStorage( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_STORAGE.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputStorage( output ).toUpperCase();
    }

    public String getAndroidId ( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_ANDROID_ID.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputAndroidId( output ).toUpperCase();
    }

    public String getDisplay( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_DISPLAY.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputDisplay( output ).toUpperCase();
    }

    public String getRam( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_RAM.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputRam( output ).toUpperCase();
    }

    public String getProcessor( Device device ) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_PROCESSOR.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputProcessor( output ).toUpperCase();
    }

    public List<String> getContacts( Device device ){
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_CONTACTS.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand( command );
        return adbParsers.parseOutputContacts( output );
    }

    public void callPhone(Device device, String number){
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_CALL_PHONE.getCommand(device.getDeviceName(), number) );
        adbService.executeCommand( command );
    }

    public List<String> getPackages(Device device){
        String manufacturer = getManufacturer(device);
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_PACKAGES.getCommand(device.getDeviceName()) );
        String output = adbService.executeCommand(command);
        return adbParsers.parseOutputPackagesUser(output, manufacturer);
    }

    public String getPackageInfo(Device device, String pack) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_PACKAGES_INFO.getCommand(device.getDeviceName(), pack));
        return adbService.executeCommand(command);
    }

    public String dropApp(Device device, String pack) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ABD_UNINSTALL.getCommand(device.getDeviceName(), pack));
        String output = adbService.executeCommand(command);
        return output;
    }

    public String getPathApp(Device device, String pack) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_PACK_PATH.getCommand(device.getDeviceName(), pack));
        String output = adbService.executeCommand(command);
        return adbParsers.pareOutputPathBaseApk(output);
    }

    public String pull(Device device, String from, String to) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_PULL.getCommand(device.getDeviceName(), from, to));
        String output = adbService.executeCommand(command);
        return adbParsers.pareOutputPathBaseApk(output);
    }

    public String install(Device device, String apkPath) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_INSTALL.getCommand(device.getDeviceName(), apkPath));
        return adbService.executeCommand(command);
    }

    public List<String> ls(Device device, String path) {
        String filesOnFolder = getFilesFrom(device, path);

        if (filesOnFolder.isEmpty())
            return null;

        List<String> files = List.of(filesOnFolder.split(","));
        List<String> outputFiles = new ArrayList<>();

        for (String file : files) {
            String commandText = AdbCommands.ADB_STAT.getCommand(device.getDeviceName()) + " '%n,%s,%U,%y,%A' " +  path + file.replaceAll(" ", "");
            List<String> command = adbCommandBuilder.buildCommand(commandText);
            String output = adbService.executeCommand(command);
            if (output != null && !output.isEmpty())
                outputFiles.add(adbParsers.parseOutputStat(output));
        }

        return outputFiles;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getFilesFrom(Device device, String from) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_LS_M.getCommand(device.getDeviceName(), from));
        return adbService.executeCommand(command).replaceAll("\n", "").replaceAll("\r", "");
    }

    public boolean mkdir(Device device, String nameFolder, String path) {
        List<String> command = adbCommandBuilder.buildCommand( AdbCommands.ADB_MKDIR.getCommand(device.getDeviceName(), path + nameFolder));
        String output = adbService.executeCommand(command);
        return output.contains("created directory");
    }

}
