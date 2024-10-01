package org.hq.androidtool.adb;

import java.util.List;

public class AdbCommandBuilder {

    public static List<String> buildCommand(String args) {
        return List.of(args.split(" "));
    }

}
