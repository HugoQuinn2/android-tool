package org.hq.androidtool.services;

import lombok.NoArgsConstructor;
import org.hq.androidtool.config.AdbConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class AdbService {

    public String executeCommand(List<String> command){
        try {
            LinkedList<String> commandAdb = new LinkedList<>(command);
            commandAdb.addFirst(AdbConfig.ADB_PATH );

            Process process = new ProcessBuilder(commandAdb).start();
            BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            StringBuilder output = new StringBuilder();
            String line;

            while ( (line = reader.readLine()) != null ) {
                output.append(line).append("\n");
            }

            return output.toString();
        } catch (Exception e){
            System.err.println("Command Error: " + e);
        }

        return null;
    }

}
