package org.hq.androidtool.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class Application {
    private String name;
    private String packageName;
    private String size;
    private String path;
}
