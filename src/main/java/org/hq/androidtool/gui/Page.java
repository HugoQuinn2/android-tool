package org.hq.androidtool.gui;

import javafx.scene.control.Tab;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hq.androidtool.constants.PagesType;
import org.hq.androidtool.device.Device;

@Data
@Builder
@EqualsAndHashCode
public class Page {
    private Device device;
    private PagesType pagesType;
    private Tab tab;
}
