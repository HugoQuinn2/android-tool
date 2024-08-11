package org.hq.androidtool.config;

import java.io.File;

public class AdbConfig {
    private static final String APP_CONFIG_DIR = System.getProperty("user.dir");
    public static final String ADB_PATH = APP_CONFIG_DIR + File.separator + "adb" + File.separator + "adb.exe";
}
