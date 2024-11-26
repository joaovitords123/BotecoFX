package com.example.botecofx;

import com.example.botecofx.db.entidades.Comanda;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ComandaController {
    public AnchorPane anchorPane;
    public Label lbNumCommand;
    public Label lbValor;
    public int idComanda;
    static public int id;

    public void onGerenciarComanda(MouseEvent mouseEvent) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("comanda-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        ComandaFormController cmd = fxmlLoader.getController();
        cmd.carregarComanda(idComanda);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void setComanda(Comanda comanda) {
        // Buscar o numero da comanda pelo id recebido
        this.id = comanda.getNumero();
        idComanda = comanda.getId();
        lbNumCommand.setText(""+id);
        // Atualizar o valor
        lbValor.setText("" + (comanda.getValor() - new ComandaFormController().valorPago));
    }

    public void setNovaComanda() {
        lbNumCommand.setText("+");
        lbNumCommand.setFont(Font.font(40));
        anchorPane.setStyle("-fx-background-color: #008F39; -fx-background-radius: 16; -fx-border-radius: 16;");
        // ARRUMAR O EVENTO DE CLIQUE
        anchorPane.setOnMouseClicked(ev -> {
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("comanda-cad-view.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            new ComandaPainelController().carregarComandas();
        });
        lbValor.setText("");
    }

    public void setComandaVazia() {
        lbNumCommand.setText("Não há comandas fechadas");
        lbValor.setText("");
    }

    public void setComandaFechada(Comanda comanda) {
        lbNumCommand.setText(""+ comanda.getNumero());
        lbValor.setText(""+comanda.getValor());
        anchorPane.setStyle("-fx-background-color: salmon; -fx-background-radius: 16; -fx-border-radius: 16;");
        idComanda = comanda.getId();
    }

    public void setComandaVaziaData() {
        lbNumCommand.setText("Não há comandas nesta data");
        lbValor.setText("");
    }
}
