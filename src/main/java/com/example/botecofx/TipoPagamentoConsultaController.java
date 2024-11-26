package com.example.botecofx;

import com.example.botecofx.db.dals.TipoPagamentoDAL;
import com.example.botecofx.db.entidades.Garcom;
import com.example.botecofx.db.entidades.TipoPagamento;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TipoPagamentoConsultaController implements Initializable {

    @FXML
    private TableColumn<TipoPagamento, String> col_id;

    @FXML
    private TableColumn<TipoPagamento, String> col_nome;

    @FXML
    private TableView<TipoPagamento> tabela;

    @FXML
    private TextField tfFiltro;

    private TipoPagamentoDAL tipoPagamentoDAL;
    public static TipoPagamento tipoPagamento;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoPagamentoDAL = new TipoPagamentoDAL();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preencherTabela("");
    }

    private void preencherTabela(String filtro) {
        List<TipoPagamento>tipoPagamentoList=tipoPagamentoDAL.get(filtro);
        tabela.setItems(FXCollections.observableArrayList(tipoPagamentoList));
    }

    @FXML
    void onAlterar(ActionEvent event) throws IOException {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            tipoPagamento=tabela.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("tipopagamento-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            tipoPagamento=null;
            preencherTabela("");
            tfFiltro.setText("");
        }
    }

    @FXML
    void onExcluir(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            TipoPagamento tipoPagamento=  tabela.getSelectionModel().getSelectedItem();
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja excluir o Tipo de pagamento "+tipoPagamento.getNome());
            if(alert.showAndWait().get()== ButtonType.OK){
                tipoPagamentoDAL.apagar(tipoPagamento);
                preencherTabela("");
                onFiltro(null);
            }
        }
    }


    @FXML
    void onFechar(ActionEvent event) {
        tfFiltro.getScene().getWindow().hide();
    }

    @FXML
    void onFiltro(KeyEvent event) {
        String filtro= tfFiltro.getText().toUpperCase();
        preencherTabela("upper(tpg_nome) LIKE'"+filtro+"'");
    }

    @FXML
    void onNovo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("tipopagamento-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        preencherTabela("");
        tfFiltro.setText("");
    }

}
