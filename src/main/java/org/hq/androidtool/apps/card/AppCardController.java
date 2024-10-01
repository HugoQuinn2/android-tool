package org.hq.androidtool.apps.card;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import org.hq.androidtool.gui.GuiConfig;
import org.hq.androidtool.apps.Application;
import org.hq.androidtool.services.PlayStoreService;

import java.io.File;
import java.net.MalformedURLException;

@Data
public class AppCardController {
    @FXML
    private Label lblAppName;
    @FXML
    private Label lblAppPack;
    @FXML
    private ImageView imgAppImage;

    private Application application;
    private String urlImg;

    public AppCardController(Application application){
        this.application = application;
    }

    @FXML
    public void initialize() throws MalformedURLException {
        if (application != null) {
            lblAppName.setText(application.getName());
            lblAppPack.setText(application.getPackageName());
        }

        File imgFile = new File(GuiConfig.APP_DEFAULT_IMG_PATH);
        imgAppImage.setImage(new Image(String.valueOf(imgFile.toURI().toURL())));
//        searchImage();
    }

    private void searchImage() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                PlayStoreService playStoreService = new PlayStoreService(application.getPackageName());
                urlImg = playStoreService.getUrlImgApp();

                Platform.runLater(() -> setImage());
                return null;
            }
        };

        task.setOnFailed(e -> {
            Platform.runLater(() -> {
                System.err.println(e);
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void setImage() {
        if (urlImg != null) {
            System.out.println("Cambiando imaguen de icono: " + application.getPackageName());
            imgAppImage.setImage(new Image(urlImg));
        }
    }
}
