package org.hq.androidtool.utils;

import java.util.List;

public class AdbCommandBuilder {

    public static List<String> buildCommand(String args) {
        return List.of(args.split(" "));
    }

}
