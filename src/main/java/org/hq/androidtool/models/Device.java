package org.hq.androidtool.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hq.androidtool.config.DevicesState;

@Data
@Builder
@EqualsAndHashCode
public class Device {
    private String deviceName;
    private DevicesState deviceState;
}
