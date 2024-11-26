package com.example.botecofx;

import com.example.botecofx.db.dals.ProdutoDAL;
import com.example.botecofx.db.dals.UnidadeDAL;
import com.example.botecofx.db.entidades.Produto;
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

public class ProdutoConsultaController implements Initializable {

    public TableColumn<Produto,String> col_preco;
    public TableColumn<Produto,String> col_desc;
    @FXML
    private TableColumn<Produto,String> col_id;

    @FXML
    private TableColumn<Produto,String> col_nome;

    @FXML
    private TableView<Produto> tabela;

    @FXML
    private TextField tfFiltro;

    private ProdutoDAL produtoDAL;
    public static Produto produto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        produtoDAL = new ProdutoDAL();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        col_desc.setCellValueFactory(new PropertyValueFactory<>("descr"));
        preencherTabela("");
    }

    private void preencherTabela(String filtro) {
        List<Produto> produtoList= produtoDAL.get(filtro);
        tabela.setItems(FXCollections.observableArrayList(produtoList));
    }

    @FXML
    void onAlterar(ActionEvent event) throws IOException {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            produto= tabela.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("produto-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            produto=null;
            preencherTabela("");
            tfFiltro.setText("");
        }
    }

    @FXML
    void onExcluir(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            Produto produto= tabela.getSelectionModel().getSelectedItem();
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja excluir o produto "+produto.getNome());
            if(alert.showAndWait().get()== ButtonType.OK){
                produtoDAL.apagar(produto);
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
        preencherTabela("upper(prod_nome) LIKE'"+filtro+"'");
    }

    @FXML
    void onNovo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("produto-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        preencherTabela("");
        tfFiltro.setText("");
    }

}
