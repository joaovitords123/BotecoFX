package com.example.botecofx;

import com.example.botecofx.db.dals.CategoriaDAL;
import com.example.botecofx.db.dals.UnidadeDAL;
import com.example.botecofx.db.entidades.Categoria;
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

public class CategoriaConsultaController implements Initializable {

    @FXML
    private TableColumn<Categoria, String> col_id;

    @FXML
    private TableColumn<Categoria, String> col_nome;

    @FXML
    private TableView<Categoria> tabela;

    @FXML
    private TextField tfFiltro;
    private CategoriaDAL categoriaDAL;
    public static Categoria categoria;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoriaDAL = new CategoriaDAL();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preencherTabela("");
    }

    private void preencherTabela(String filtro) {
        List<Categoria> categoriaList= categoriaDAL.get(filtro);
        tabela.setItems(FXCollections.observableArrayList(categoriaList));
    }

    @FXML
    void onAlterar(ActionEvent event) throws IOException {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            categoria=tabela.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("categoria-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            categoria=null;
            preencherTabela("");
            tfFiltro.setText("");
        }
    }

    @FXML
    void onExcluir(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            Categoria categoria= tabela.getSelectionModel().getSelectedItem();
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja excluir o Categoria "+categoria.getNome());
            if(alert.showAndWait().get()== ButtonType.OK){
                categoriaDAL.apagar(categoria);
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
        preencherTabela("upper(cat_nome) LIKE"+filtro);
    }

    @FXML
    void onNovo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("categoria-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        preencherTabela("");
        tfFiltro.setText("");

    }

}
