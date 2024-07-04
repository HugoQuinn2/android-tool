package org.hq.androidtool.adb;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class adbModel {
    private String adbPath;
    private String message;
    private String stateDevice;
    private String toolLevel;
    private ProcessBuilder deviceModel;
    private ProcessBuilder wlan;
    private ProcessBuilder ipDevice;
    private ProcessBuilder deleteApplication;
    private ProcessBuilder installApplication;
    private ProcessBuilder pullFile;
    private ProcessBuilder pushFile;
    private ProcessBuilder deleteFile;
    private ProcessBuilder device;
    private ProcessBuilder serialNumber;
    private ProcessBuilder androidDevice;
    private ProcessBuilder packages;
    private ProcessBuilder root;
    private ProcessBuilder fileInfo;
    private ProcessBuilder androidVersion;
    private ProcessBuilder phoneNumber;
    private ProcessBuilder simContract;
    private ProcessBuilder manufacturer;
    private ProcessBuilder storage;

    public adbModel(String adbPath){
        this.adbPath = adbPath;
        this.root = new ProcessBuilder(adbPath, "root");
        this.deviceModel = new ProcessBuilder(adbPath, "shell", "getprop", "ro.product.model");
        this.wlan = new ProcessBuilder(adbPath, "shell", "ip", "addr", "show", "wlan0");
        this.ipDevice = new ProcessBuilder(adbPath, "shell", "ip", "addr", "show", "wlan0");
        this.device = new ProcessBuilder(adbPath, "devices");
        this.serialNumber = new ProcessBuilder(adbPath, "get-serialno");
        this.androidDevice = new ProcessBuilder(adbPath, "shell", "getprop", "ro.build.version.release");
        this.packages = new ProcessBuilder(adbPath, "shell", "pm", "list", "packages");
        this.fileInfo = new ProcessBuilder(adbPath, "shell", "ls", "-lh");
        this.androidVersion = new ProcessBuilder(adbPath, "shell", "getprop", "ro.build.version.release");
        this.phoneNumber = new ProcessBuilder(adbPath, "shell", "dumpsys", "telephony.registry");
        this.simContract = new ProcessBuilder(adbPath, "shell", "getprop", "gsm.sim.operator.alpha");
        this.manufacturer = new ProcessBuilder(adbPath, "shell", "getprop", "ro.product.manufacturer");
        this.storage = new ProcessBuilder(adbPath, "shell", "df");
    }

    public void setDeleteApplication(String packageApplication) {
        this.deleteApplication = new ProcessBuilder(adbPath, "uninstall", packageApplication);
    }

    public void setInstallApplication(String pathApplication){
        this.installApplication = new ProcessBuilder(adbPath, "install", pathApplication);
    }

    public void setDeleteFile(String filePath){
        this.deleteFile = new ProcessBuilder(adbPath, "shell", filePath);
    }

    public void setPushFile(String originPath, String pushPath){
        this.pushFile = new ProcessBuilder(adbPath, "push", originPath, pushPath);
    }

    public void setPullFile(String originPath, String pullPath ){
        this.pullFile = new ProcessBuilder(adbPath, "pull", originPath, pullPath);
    }

    public ProcessBuilder getFileInfo(String path){
        fileInfo.command().add(path);
        return fileInfo;
    }

}
