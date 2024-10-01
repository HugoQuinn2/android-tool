package org.hq.androidtool.gui.alert;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;

@Getter
@Setter
public class AlertPageController {
    @FXML private TextArea notificationLabel;
    @FXML private HBox notificationPane;
    @FXML private FontIcon iconAlert;
    @FXML private ProgressBar pbTimer;
    @FXML private Label alertTitle;

    private AlertController alertController;

    @FXML
    public void dismissNotification() {
        alertController.dropFxml();
    }
}
