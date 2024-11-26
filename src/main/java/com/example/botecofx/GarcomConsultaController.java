package com.example.botecofx;

import com.example.botecofx.db.dals.GarcomDAL;
import com.example.botecofx.db.entidades.Garcom;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GarcomConsultaController implements Initializable {
    @FXML
    private TableColumn<Garcom, String> colCidade;

    @FXML
    private TableColumn<Garcom, String> colFone;

    @FXML
    private TableColumn<Garcom, String> colId;

    @FXML
    private TableColumn<Garcom, String> colNome;

    @FXML
    private TableView<Garcom> tabela;

    @FXML
    private TextField tfFiltro;
    public static Garcom garcom;
    private GarcomDAL garcomDal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        garcomDal = new GarcomDAL();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colFone.setCellValueFactory(new PropertyValueFactory<>("fone"));
        preencherTabela("");
    }

    private void preencherTabela(String filtro) {
        List<Garcom> garcomList = garcomDal.get(filtro);
        tabela.setItems(FXCollections.observableArrayList(garcomList));
    }

    @FXML
    void onAlterar(ActionEvent event) throws Exception {
        if(tabela.getSelectionModel().getSelectedIndex()>=0) {
            garcom = tabela.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("garcom-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            garcom = null;
            tfFiltro.setText("");
            preencherTabela("");
        }
    }

    @FXML
    void onExcluir(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex()>=0) {
            Garcom garcom = tabela.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja excluir o garcom " + garcom.getNome());
            if(alert.showAndWait().get() == ButtonType.OK) {
                garcomDal.apagar(garcom);
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
        String filtro = tfFiltro.getText();
        preencherTabela("upper(gar_nome) LIKE '"+filtro+"'");
    }

    @FXML
    void onNovo(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("garcom-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        tfFiltro.setText("");
        stage.showAndWait();
        preencherTabela("");
    }
}
