package org.hq.androidtool.constants;

import lombok.Getter;

public enum Messages {
    CREATE_FOLDER_SUCCESS("Creacion de Carpeta", "La carpeta %s se creao correctamente en %s"),
    CREATE_FOLDER_ERROR("Creacion de Carpeta", "Error: %s"),
    DROP_APP_SUCCESS("Borrador de Aplicacion", "La aplicacion %s se borro exitosamente."),
    DROP_APP_ERROR("Borrador de Aplicacion", "La aplicacion %s no se pudo borrar."),
    INSTALL_APP_SUCCESS("Instalacion de aplicacion", "La aplicacion %s se instalo correctamente"),
    INSTALL_APP_ERROR("Instalacion de aplicacion", "La aplicacion %s no se pudo instalar"),
    INSTALL_APP_INFO("Instalacion de aplicacion", "Instalando %s"),
    DOWNLOAD_APP_SUCCESS("Descarga de aplicacion", "La aplicacion %s se descargo correctamente."),
    DOWNLOAD_APP_ERROR("Descarga de aplicacion", "La aplicacion %s no se pudo descargar."),
    DOWNLOAD_APP_INFO("Descarga de aplicacion", "Descargando %s.");

    @Getter
    private final String title;
    private final String message;

    Messages(String title, String message) {
        this.message = message;
        this.title = title;
    }

    public String getMessage(String... data) {
        return String.format(this.message, (Object[]) data);
    }

}
