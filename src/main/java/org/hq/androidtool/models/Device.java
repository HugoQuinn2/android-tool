package org.hq.androidtool.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hq.androidtool.constants.DevicesState;

@Data
@Builder
@EqualsAndHashCode
public class Device {
    private String deviceName;
    private DevicesState deviceState;
}
