package org.hq.androidtool.adb;

import org.hq.androidtool.models.FileInfoModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class adbController {
    private final org.hq.androidtool.adb.adbModel adbModel;
    private Process process;

    public adbController(){
        adbModel = new adbModel(getClass().getResource("/org/hq/androidtool/adbAndroid/adb.exe").getPath());
    }

    /***** Control and connection device functions *****/
    private boolean executeCommand(ProcessBuilder processBuilder){
        try {
            //Iniciar el proceso enviado
            //processBuilder.redirectErrorStream(true);
            process = processBuilder.start();

            //Obtener salida del comando y guardarlo
            List<String> message = new ArrayList<>();
            String line = null;

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            while ( (line = bufferedReader.readLine()) != null ){
                message.add(line);
            }
            process.waitFor();
            adbModel.setMessage(message.toString());
            return true;
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return false;
        }
    } // Execute and recompile message command
    public void device(){
        if(getRoot()){
            adbModel.setToolLevel("root");
        } else {
            adbModel.setToolLevel("user");
        }

        executeCommand(adbModel.getDevice());
        adbModel.setStateDevice("no device");

        String message = adbModel.getMessage().toLowerCase().split(",")[1];

        if ( message.contains("unauthorized") ){
            adbModel.setStateDevice("unauthorized");

        } else if ( message.contains("device") ){
            adbModel.setStateDevice("device");

        } else if (message.contains("offline")){
            adbModel.setStateDevice("offline");
        }
    } // Execute adb like root and state connection

    /******* Information device functions ********/
    public boolean getRoot(){
        executeCommand(adbModel.getRoot());

        for (String word : adbModel.getMessage().split("\\s+")){
            if (adbModel.getMessage().contains("cannot run as root")
                    || adbModel.getMessage().contains("unable to connect for root")
                    || adbModel.getMessage() == "[]" ) {
                return false;
            }
        }
        return true;
    }
    public String getDeviceName(){
        String deviceName = null;
        String pat = "(?<=List of devices attached,\\s)(\\w+)(?=\\s+" + adbModel.getStateDevice() + ")";

        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(adbModel.getMessage());

        return deviceName = matcher.find() ? matcher.group() : null;
    } // Extract device name from command device
    public boolean getStateDevice(){
        return Objects.equals(adbModel.getStateDevice(), "device");
    } // Define if the device is useful
    public String getDeviceModel(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }
        executeCommand(adbModel.getDeviceModel());

        return adbModel.getMessage().replaceAll("[\\[\\]]", "");
    } // Execute command model and extract model
    public String getSerialNumber(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }
        executeCommand(adbModel.getSerialNumber());
        return adbModel.getMessage().replaceAll("[\\[\\]]", "");
    } // Execute command serial number and extract sn
    public String getMacDevice(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }
        String macDevice = null;
        executeCommand(adbModel.getWlan());

        String pat = "link/ether ([0-9a-fA-F:]{17})";

        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(adbModel.getMessage());

        return macDevice = matcher.find() ? matcher.group().replace("link/ether ", "") : null;
    } // Execute command mac and extract mac device
    public String getIpDevice(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }
        String ipDevice = null;
        executeCommand(adbModel.getWlan());

        String pat = "inet ([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";

        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(adbModel.getMessage());

        return ipDevice = matcher.find() ? matcher.group().replace("inet ", "") : null;
    } // Execute command ip and extract ip device
    public List<String> getApplicationsDevice(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }

        List<String> listPackages = new ArrayList<>();
        executeCommand(adbModel.getPackages());
        String message = adbModel.getMessage().substring(1, adbModel.getMessage().length() - 1);
        String[] packages = message.split(",");

        for (String pack : packages){
            if (!pack.startsWith(" package:com.android.") &&
                    !pack.startsWith(" package:com.google.android.") &&
                    !pack.startsWith("package:com.sec.android.")){
                listPackages.add(pack.replace("package:", ""));
            }
        }

        return listPackages;
    } // Execute command applications device and filter native applications
    public List<FileInfoModel> getFileInfoDevice(String path){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }

        List<FileInfoModel> fileInfoModelList = new ArrayList<>(List.of());
        List<String> fileInfo;

        executeCommand(adbModel.getFileInfo(path));
        fileInfo = List.of(adbModel.getMessage()
                .replaceAll("[\\[\\]]", "")
                .replaceAll("\\s+", " ").trim()
                .split(","));

        for (String f: fileInfo){
            List<String> fileData = List.of(f.replaceFirst("^\\s+", "").split(" "));

            if (fileData.size() >= 7 && !Objects.equals(fileData.get(1), "?")){
                FileInfoModel fileInfoModel = FileInfoModel
                        .builder()
                        .isDirectory(fileData.get(0).startsWith("d"))
                        .user(fileData.get(2))
                        .size(fileData.get(4))
                        .date(fileData.get(5) + " " + fileData.get(6))
                        .filePath(path + "/" + fileData.get(7))
                        .isEnabled(Objects.equals(adbModel.getToolLevel(), fileData.get(2)))
                        .build();

                fileInfoModelList.add(fileInfoModel);
            }
        }

        return fileInfoModelList;
    }
    public String getAndroidVersion(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }
        executeCommand(adbModel.getAndroidVersion());

        return adbModel.getMessage().replaceAll("[\\[\\]]", "");
    }
    public String getSimContract(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }
        executeCommand(adbModel.getSimContract());

        return adbModel.getMessage().replaceAll("[\\[\\]]", "");
    }
    public String getManufacturer(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }
        executeCommand(adbModel.getManufacturer());

        return adbModel.getMessage().replaceAll("[\\[\\]]", "");
    }
    public String getStorage(){
        if (!Objects.equals(adbModel.getStateDevice(), "device")){
            return null;
        }
        executeCommand(adbModel.getStorage());

        int blocks = 0;
        int used = 0;
        boolean flat = false;

        List<String> storageList = List.of(adbModel.getMessage()
                .replaceAll("[\\[\\]]", "")
                .replaceAll("\\s+", " ").trim()
                .split(","));

        for (String storage : storageList){
            List<String> stor = List.of(storage.replaceFirst("^\\s+", "").split(" "));
            if (!flat) {
                flat = true;
                continue; // Salta al siguiente ciclo
            }

            if(stor.get(5).contains("emulated")){
                continue;
            }
            blocks += Integer.parseInt(stor.get(1));
            used += Integer.parseInt(stor.get(2));
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(false);

        double totalStorage = Double.parseDouble(
                df.format( blocks / 1000 / 1000));
        double totalStorageUsed = Double.parseDouble(
                df.format( used / 1000 / 1000));

        return (totalStorage + "/" + totalStorageUsed);
    }
}
