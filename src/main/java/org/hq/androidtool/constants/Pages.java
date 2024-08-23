package org.hq.androidtool.constants;

public enum Pages {
    MENU_PAGE_TITLE     (PagesType.MENU, "MENU", "bi-house-fill"),
    APPS_PAGE_TITLE     (PagesType.APPS, "APPS", "bi-grid-fill"),
    FILES_PAGE_TITLE    (PagesType.FILES, "FILES", "bi-folder-fill"),
    CONTACTS_PAGE_TITLE (PagesType.CONTACTS , "CONTACTS", "bi-people-fill");

    private final PagesType type;
    private final String title;
    private final String icon;

    Pages(PagesType type, String title, String icon){
        this.type = type;
        this.title = title;
        this.icon = icon;
    }

    public static String getTitleByType(PagesType type) {
        for (Pages detail : values()) {
            if (detail.type == type) {
                return detail.title;
            }
        }
        return null;
    }

    public static String getIconByType(PagesType type) {
        for (Pages detail : values()) {
            if (detail.type == type) {
                return detail.icon;
            }
        }
        return null;
    }
}
