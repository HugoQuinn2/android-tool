package org.hq.androidtool.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hq.androidtool.config.FilesType;
import org.kordamp.ikonli.javafx.FontIcon;

@Data
@Builder
@EqualsAndHashCode
public class FileDevice {
    private String name;
    private String user;
    private String size;
    private String date;
    private FilesType fileType;
    private String path;

    public FontIcon getFileIcon() {
        switch (this.fileType) {
            case FOLDER:
                return new FontIcon("bi-folder-fill");
            case FILE:
                return new FontIcon("bi-file-text-fill");
            case INDETERMINATE:
                return new FontIcon("bi-question");
            case SYMBOLIC_LINK:
                return new FontIcon("bi-folder-symlink");
            default:
                return new FontIcon("bi-file");
        }
    }
}
