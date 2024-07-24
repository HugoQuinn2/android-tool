package org.hq.androidtool.controllers.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import lombok.Data;
import org.hq.androidtool.models.Application;

@Data
public class AppCardController {
    @FXML
    private Label lblAppName;

    @FXML
    private Label lblAppPack;

    private Application application;

    public AppCardController(Application application){
        this.application = application;
    }

    @FXML
    public void initialize(){
        if (application != null) {
            lblAppName.setText(application.getName());
            lblAppPack.setText(application.getPackageName());
        }
    }
}
