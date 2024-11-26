package com.example.botecofx;

import com.example.botecofx.db.dals.ComandaDAL;
import com.example.botecofx.db.entidades.Comanda;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComandaFechadaController implements Initializable {
    public DatePicker dataCom;
    private ComandaDAL comandaDAL = new ComandaDAL();
    public FlowPane flowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataCom.setValue(null);
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
        int cont = 0;
        List<Comanda> comandas = buscarComandas();
        try {
            if(dataCom.getValue()==null) {
                for (int i = 0; i < comandas.size(); i++) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-view.fxml"));
                    Parent root = (Parent) loader.load();
                    ComandaController ctr = loader.getController();
                    if (comandas.get(i).getStatus() == 'F') {
                        cont++;
                        ctr.setComandaFechada(comandas.get(i));
                        flowPane.getChildren().add(root);
                    }
                }
                if (cont == 0) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-view.fxml"));
                    Parent root = (Parent) loader.load();
                    ComandaController ctr = loader.getController();
                    ctr.setComandaVazia();
                }
            }
            else {
                for (int i = 0; i < comandas.size(); i++) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-view.fxml"));
                    Parent root = (Parent) loader.load();
                    ComandaController ctr = loader.getController();
                    if (comandas.get(i).getStatus() == 'F' && comandas.get(i).getData().equals(dataCom.getValue())) {
                        cont++;
                        ctr.setComandaFechada(comandas.get(i));
                        flowPane.getChildren().add(root);
                    }
                }
                if (cont == 0) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-view.fxml"));
                    Parent root = (Parent) loader.load();
                    ComandaController ctr = loader.getController();
                    ctr.setComandaVaziaData();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onRemoverFiltro(ActionEvent actionEvent) {
        dataCom.setValue(null);
        flowPane.getChildren().clear();
        carregarComandas();
    }

    public void onBuscarData(ActionEvent actionEvent) {
        System.out.println(dataCom.getValue());
        flowPane.getChildren().clear();
        carregarComandas();
    }
}
