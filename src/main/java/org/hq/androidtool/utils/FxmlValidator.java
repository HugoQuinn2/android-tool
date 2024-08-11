package org.hq.androidtool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

public class FxmlValidator {
    private static final List<String> UNSAFE_TAGS = List.of("Script", "HttpRequest", "JavaScript");
    private static final List<String> UNSAFE_ATTRIBUTES = List.of("onload", "onclick");
    private static final Logger logger = LoggerFactory.getLogger(FxmlValidator.class);

    public static boolean isFxmlSafe(URL fxmlUrl) {
        return validateFxmlContent(fxmlUrl);
    }

    private static boolean validateFxmlContent(URL fxmlUrl) {
        try {
            File fxmlFile = new File(fxmlUrl.toURI());
            String content = new String(java.nio.file.Files.readAllBytes(fxmlFile.toPath()));

            return !containsUnsafeTags(content) && !containsUnsafeAttributes(content);
        } catch (Exception e) {
            logger.error("Error fxml loader secure: " + e.getCause() + e.getMessage());
            return false;
        }
    }

    private static boolean containsUnsafeTags(String content) {
        return UNSAFE_TAGS.stream().anyMatch(content::contains);
    }

    private static boolean containsUnsafeAttributes(String content) {
        return UNSAFE_ATTRIBUTES.stream().anyMatch(content::contains);
    }
}
