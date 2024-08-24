package org.hq.androidtool.constants;

public enum AdbCommands{
    ADB_DEVICE          ("devices"),
    ADB_WLAN0           ("-s %s shell ip addr show wlan0"),
    ADB_MODEL           ("-s %s shell getprop ro.product.model"),
    ADB_SERIAL          ("-s %s get-serialno"),
    ADB_ANDROID_VERSION ("-s %s shell getprop ro.build.version.release"),
    ADB_SIM_CONTRACT    ("-s %s shell getprop gsm.sim.operator.alpha"),
    ADB_MANUFACTURER    ("-s %s shell getprop ro.product.manufacturer"),
    ADB_STORAGE         ("-s %s shell df"),
    ADB_ANDROID_ID      ("-s %s shell settings get secure android_id"),
    ADB_DISPLAY         ("-s %s shell wm size"),
    ADB_PROCESSOR       ("-s %s shell cat /proc/cpuinfo"),
    ADB_RAM             ("-s %s shell cat /proc/meminfo"),
    ADB_CONTACTS        ("-s %s shell content query --uri content://contacts/phones/"),
    ADB_CALL_PHONE      ("-s %s shell am start -a android.intent.action.CALL -d tel:%s"),
    ADB_PACKAGES        ("-s %s shell pm list packages"),
    ADB_PACKAGES_INFO   ("-s %s shell dumpsys package %s"),
    ADB_PS              ("-s %s shell ps"),
    ADB_TOP             ("-s %s shell top -n 1"),
    ABD_UNINSTALL       ("-s %s uninstall %s"),
    ADB_INSTALL         ("-s %s install %s"),
    ADB_PACK_PATH       ("-s %s shell pm path %s"),
    ADB_PULL            ("-s %s pull %s %s"),
    ADB_LS              ("-s %s shell cd %s && ls -l"),
    ADB_LS_M            ("-s %s shell ls -m %s"),
    ADB_STAT            ("-s %s shell stat -c"),
    ADB_MKDIR           ("-s %s shell mkdir %s");

    private final String command;

    AdbCommands(String command){
        this.command = command;
    }

    public String getCommand(String... data){
        return String.format( command, (Object[]) data );
    }
}
