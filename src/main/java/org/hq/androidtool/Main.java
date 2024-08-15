package org.hq.androidtool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        try {
            MainApp.main(args);
        } catch (Exception e) {
            logger.error("Error ejecutando la aplicacion: " , e);
        }

    }
}
