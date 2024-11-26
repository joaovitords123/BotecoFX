package com.example.botecofx;

import com.example.botecofx.db.dals.UnidadeDAL;
import com.example.botecofx.db.entidades.TipoPagamento;
import com.example.botecofx.db.entidades.Unidade;
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

public class UnidadeConsultaController implements Initializable {

    @FXML
    private TableColumn<Unidade, String> col_id;

    @FXML
    private TableColumn<Unidade, String> col_nome;

    @FXML
    private TableView<Unidade> tabela;

    @FXML
    private TextField tfFiltro;

    private UnidadeDAL unidadeDAL;
    public static Unidade unidade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        unidadeDAL = new UnidadeDAL();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preencherTabela("");
    }

    @FXML
    void onAlterar(ActionEvent event) throws IOException {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            unidade=tabela.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("unidade-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            unidade=null;
            preencherTabela("");
            tfFiltro.setText("");
        }
    }

    @FXML
    void onExcluir(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            Unidade unidade= tabela.getSelectionModel().getSelectedItem();
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja excluir a Unidade "+unidade.getNome());
            if(alert.showAndWait().get()== ButtonType.OK){
                 unidadeDAL.apagar(unidade);
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
        preencherTabela("upper(uni_nome) LIKE'"+filtro+"'");
    }

    private void preencherTabela(String filtro) {
        List<Unidade> unidadeList= unidadeDAL.get(filtro);
        tabela.setItems(FXCollections.observableArrayList(unidadeList));
    }


    @FXML
    void onNovo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("unidade-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        preencherTabela("");
        tfFiltro.setText("");
    }

}
