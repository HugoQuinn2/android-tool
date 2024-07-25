package org.hq.androidtool.utils;

import org.hq.androidtool.config.DevicesState;
import org.hq.androidtool.config.FilesType;

import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdbParsers {

    public static List<String> parseOutputDevice( String output ){
        List<String> lines = List.of(output.split("\n"));
        List<String> devices = new ArrayList<>();

        Pattern patternDevice = Pattern.compile("(.*?)\\tdevice");
        Pattern patternUnauthorized = Pattern.compile("(.*?)\\tunauthorized");
        Pattern patternNoDevice = Pattern.compile("(.*?)\\tno device");

        for (String line : lines) {
            Matcher matcherDevice = patternDevice.matcher(line);
            Matcher matcherUnauthorized = patternUnauthorized.matcher(line);
            Matcher matcherNoDevice = patternNoDevice.matcher(line);

            if (matcherDevice.find()) {
                devices.add(matcherDevice.group(1).trim() + "," + DevicesState.DEVICE);
            } else if (matcherUnauthorized.find()) {
                devices.add(matcherUnauthorized.group(1).trim() + "," + DevicesState.UNAUTHORIZED);
            } else if (matcherNoDevice.find()) {
                devices.add(matcherNoDevice.group(1).trim() + "," + DevicesState.NO_DEVICE);
            }
        }

        return devices;
    }

    public String parseOutputIp( String output ){
        String pat = "inet ([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";

        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(output);

        return matcher.find() ? matcher.group(1) : null;
    }

    public String parseOutputMac( String output ) {
        String pat = "link/ether ([0-9a-fA-F:]{17})";

        Pattern pattern = Pattern.compile( pat );
        Matcher matcher = pattern.matcher( output );

        return matcher.find() ? matcher.group().replace("link/ether ", "") : null;
    }

    public String parseOutputModel( String output ) {
        return output.replaceAll("[\\[\\]]", "");
    }

    public String parseOutputSerial( String output ) {
        return output.replaceAll("[\\[\\]]", "");
    }

    public String parseOutputManufacturer( String output ) {
        return output.replaceAll("[\\[\\]]", "");
    }

    public String parseOutputAndroidVersion( String output ) {
        return output.replaceAll("[\\[\\]]", "");
    }

    public String parseOutputSimContract( String output ){
        return output.replaceAll("[\\[\\]]", "");
    }

    public String parseOutputStorage ( String output ) {
        long totalUsed = 0;
        long totalAvailable = 0;

        String[] lines = output.split("\n");

        for (String line : lines) {
            String[] columns = line.trim().split("\\s+");
            if (columns.length >= 6 && columns[0].startsWith("/dev/block/")) {
                totalUsed += Long.parseLong(columns[2]);
                totalAvailable += Long.parseLong(columns[3]);
            }
        }

        return (totalUsed + "/" + totalAvailable);
    }

    public String parseOutputAndroidId( String output ) {
        return output.replaceAll("[\\[\\]]", "");
    }

    public String parseOutputDisplay( String output ) {
        String[] lines = output.toLowerCase().split("\n");
        for (String line : lines){
            if (line.contains("physical size")){
                return line.split(":")[1].trim();
            }
        }
        return null;
    }

    public String parseOutputRam( String output ) {
        String[] lines = output.toLowerCase().split("\n");
        for (String line : lines) {
            if ( line.contains("memtotal") ) {
                return line.replaceAll("\\D+", "");
            }
        }
        return null;
    }

    public String parseOutputProcessor( String output ) {
        return output.replaceAll("[\\[\\]]", "");
    }

    public List<String> parseOutputContacts(String output){
        String[] lines = output.split("\n");
        List<String> data = new ArrayList<>();

        for (String line : lines){
            String name = null;
            String phone = null;
            String email = null;
            String id = null;

            String[] rows = line.split(",");

            for ( String row : rows ){
                name    = row.contains("display_name") ? row.split("=")[1].trim() : name;
                phone   = row.contains("number") ? row.split("=")[1].trim().replaceAll(" ","") : phone;
                email   = row.contains("primary_email") ? row.split("=")[1].trim() : email;
                id      = row.contains("_id") ? row.split("=")[1].trim() : id;
            }

            data.add(String.format("%s,%s,%s,%s", id, name, phone, email));
        }

        return data;
    }

    public List<String> parseOutputPackagesUser(String output, String manufacturer){
        List<String> lines = List.of(output.split("\n"));
        List<String> data = new ArrayList<>();
        String parseManufacturer = manufacturer
                .toLowerCase()
                .replaceAll(" ", "")
                .replaceAll("\n", "");

        for (String line : lines){
            if (!line.toLowerCase().contains("android")) {
                if (!line.toLowerCase().contains(parseManufacturer)) {
                    data.add(line.split(":")[1].trim());
                }
            }
        }

        return data;
    }

    public List<String> parseOutputPackages(String output){
        List<String> lines = List.of(output.split("\n"));
        List<String> data = new ArrayList<>();

        for (String line : lines) {
            data.add( line.split(":")[1].trim() );
        }
        return data;
    }

    public List<String> parseOutputFiles(String output) {
        List<String> lines = List.of(output.split("\n"));
        List<String> data = new ArrayList<>();
        String fileType = "";

        for (String line : lines) {
            List<String> columns = List.of(line.replaceAll("\\s+", " ").split(" "));
            if (columns.size() >= 8) {
                fileType = String.valueOf(getFyleType(columns.get(0).replaceAll(" ", "")));
                String format = String.format("%s,%s,%s,%s,%s", fileType, columns.get(2), columns.get(4), columns.get(5) + " " + columns.get(6), columns.get(7));
                data.add(format);
            }
        }

        return data;
    }

    private FilesType getFyleType(String file) {
        if (file.startsWith("d")) {
            return FilesType.FOLDER;
        } else if (file.startsWith("-")) {
            return FilesType.FILE;
        } else if (file.startsWith("i")) {
            return FilesType.SYMBOLIC_LINK;
        }

        return FilesType.INDETERMINATE;
    }
}

