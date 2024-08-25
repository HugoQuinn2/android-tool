package org.hq.androidtool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Copyright (c) 2024, Victor Hugo Gaspar Quinn
 *
 * This code is subject to the terms of the restricted-use license
 * included in the LICENSE file at the root of this repository.
 * Unauthorized use, modification, or distribution is prohibited without
 * express written permission from the author.
 */


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
