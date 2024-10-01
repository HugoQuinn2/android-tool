package org.hq.androidtool.device;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceData {
    private String serialNumber;
    private String deviceModel;
    private String macDevice;
    private String ipDevice;
    private String androidVersion;
    private String simContract;
    private String manufacturer;
    private String storage;
    private String androidId;
    private String displaySize;
    private String processor;
    private String ram;
}
