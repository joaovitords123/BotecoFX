package com.example.botecofx;

import com.example.botecofx.db.dals.ComandaDAL;
import com.example.botecofx.db.entidades.Comanda;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComandaPainelController implements Initializable {
    public FlowPane flowPane;
    private ComandaDAL comandaDAL = new ComandaDAL();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarComandas();
    }

    public List<Comanda> buscarComandas() {
        List<Comanda> comandas = new ArrayList<>();
        try {
            comandas = comandaDAL.get("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comandas;
    }


    public void carregarComandas() {
        // Obter as comandas em aberto do banco
        List<Comanda> comandas = buscarComandas();
        // Colocar as comandas na tela
        try {

            if(comandas.isEmpty()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-view.fxml"));
                Parent root = (Parent) loader.load();
                ComandaController ctr = loader.getController();
                ctr.setNovaComanda();
                flowPane.getChildren().add(root);
            }
            else {
                for (int i = 0; i <= comandas.size(); i++) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-view.fxml"));
                    Parent root = (Parent) loader.load();
                    ComandaController ctr = loader.getController();

                    if (i == 0) {
                        ctr.setNovaComanda();
                        flowPane.getChildren().add(root);
                    }
                    else {
                        if(comandas.get(i-1).getStatus()=='A') {
                            ctr.setComanda(comandas.get(i - 1));
                            flowPane.getChildren().add(root);
                        }
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onRecarregar(ActionEvent actionEvent) {
        List<Node> filhos = new ArrayList<>(flowPane.getChildren()); // Faz uma cópia para evitar modificar a lista enquanto itera

        for (Node filho : filhos) {
            // Verifica se o filho não é o botão que você quer manter
            if (!(filho instanceof Button && filho.getId().equals("btRecarrega"))) {
                flowPane.getChildren().remove(filho); // Remove o filho
            }
        }
        carregarComandas();
    }

    public void onComandaFechada(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("comandaFechada-view.fxml"));
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
    }
}
