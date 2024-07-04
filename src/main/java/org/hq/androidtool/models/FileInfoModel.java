package org.hq.androidtool.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileInfoModel {
    private String filePath;
    private boolean isDirectory;
    private boolean isEnabled;
    private String size;
    private String user;
    private String date;
}
