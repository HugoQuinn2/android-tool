package org.hq.androidtool.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hq.androidtool.config.FilesType;

import java.util.Date;

@Data
@Builder
@EqualsAndHashCode
public class File {
    private String name;
    private String user;
    private String size;
    private Date date;
    private FilesType fileType;
}
