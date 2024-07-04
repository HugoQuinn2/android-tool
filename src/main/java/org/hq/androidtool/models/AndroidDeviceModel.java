package org.hq.androidtool.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AndroidDeviceModel {
    private String serialNumber;
    private String deviceName;
    private String deviceModel;
    private String macDevice;
    private String ipDevice;
    private boolean stateDevice;
    private List<String> applicationDevice;
    private List<FileInfoModel> fileInfoModelList;
    private String androidVersion;
    private String simContract;
    private String manufacturer;
    private String storage;
}
