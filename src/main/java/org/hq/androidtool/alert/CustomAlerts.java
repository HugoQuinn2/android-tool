package org.hq.androidtool.alert;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomAlerts extends Stage {
    private int HEIGHT = 200;
    private int WIDTH = 450;
    private Button btnAccept;
    private Button btnCancel;
    private String header;
    private String content;
    private TextField textField;
    private String result;

    public CustomAlerts(String header, String content) {
        Pane root = new Pane();
        this.header = header;
        this.content = content;

        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.APPLICATION_MODAL);

        Text headerText = new Text(header);
        headerText.setFont(Font.font(20));

        VBox box = new VBox(10, headerText, createContent(), createButtons());
        box.setPadding(new Insets(10));

        root.getChildren().addAll(box);
        root.setPrefWidth(WIDTH);
        root.setPrefHeight(HEIGHT);

        setScene(new Scene(root, null));
    }

    public String getResult() {
        return result;
    }

    private Pane createButtons() {
        btnAccept = new Button("Aceptar");
        btnCancel = new Button("Cancelar");

        HBox hBox = new HBox(10, btnAccept, btnCancel);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        btnCancel.setOnAction(event -> closeAlter(null));
        btnAccept.setOnAction(event -> closeAlter(textField.getText()));

        return hBox;
    }

    private Pane createContent() {
        Text contentText = new Text(content);
        textField = new TextField();

        contentText.setFont(Font.font(16));

        HBox hBox = new HBox(10, contentText, textField);
        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    public void openAlert() {
        showAndWait();
    }

    private void closeAlter(String text) {
        result = text;
        close();
    }
}
