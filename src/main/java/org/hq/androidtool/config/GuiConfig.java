package org.hq.androidtool.config;

import java.io.File;

public class GuiConfig {
    private static final String APP_CONFIG_DIR = "/org/hq/androidtool/layout/";
    public static final String FILES_PAGE_PATH = APP_CONFIG_DIR + "content/" + "FilesPage.fxml";
    public static final String APPS_PAGE_PATH = APP_CONFIG_DIR + "content/" + "AppsPage.fxml";
    public static final String CONTACTS_PAGE_PATH = APP_CONFIG_DIR + "content/" + "ContactsPage.fxml";
    public static final String DEFAULT_PAGE_PATH = APP_CONFIG_DIR + "content/" + "DefaultPage.fxml";
    public static final String MENU_BAR_PAGE_PATH = APP_CONFIG_DIR + "content/" + "MenuBar.fxml";
    public static final String MENU_PAGE_PATH = APP_CONFIG_DIR + "content/" + "MenuPage.fxml";
    public static final String MAIN_PAGE_PATH = APP_CONFIG_DIR + "MainPage.fxml";
    public static final String APP_CARD_PATH = APP_CONFIG_DIR + "content/" + "AppCard.fxml";
    public static final String APP_DEFAULT_IMG_PATH = System.getProperty("user.dir") + File.separator + "img" + File.separator + "defaultAndroidApp.png";


    public static final String lightTheme = "/org/hq/androidtool/style/LigthTheme.css";
    public static final String darkTheme = "/org/hq/androidtool/style/DarkTheme.css";
    public static final String appIcon = APP_CONFIG_DIR + File.separator + "layout" + File.separator + "icon" + File.separator + "AppIcon.png";
    public static final String nameApp = "Android Tool";

    public static final Boolean maximized = true;
    public static final int minWidth = 0;
    public static final int minHeight = 0;
    public static final int prefWidth = 1360;
    public static final int prefHeight = 750;
}
