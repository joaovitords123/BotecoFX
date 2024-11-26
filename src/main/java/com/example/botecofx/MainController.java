package com.example.botecofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    public PasswordField pfSenha;

    public void onAcessar(ActionEvent actionEvent) {
        String view = "";
        if(pfSenha.getText().equals("adm")) {
            // usuario adm
            view = "adm-view.fxml";
        }
        if(pfSenha.getText().equals("123")) {
            // usuario adm
            view = "comanda-painel-view.fxml";
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource(view));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}