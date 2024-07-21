package org.hq.androidtool.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class Contact {
    private int id;
    private String name;
    private String phone;
    private String email;
}
