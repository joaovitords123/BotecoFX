package com.example.botecofx;

import com.example.botecofx.db.dals.CategoriaDAL;
import com.example.botecofx.db.dals.ProdutoDAL;
import com.example.botecofx.db.dals.UnidadeDAL;
import com.example.botecofx.db.entidades.Categoria;
import com.example.botecofx.db.entidades.Produto;
import com.example.botecofx.db.entidades.Unidade;
import com.example.botecofx.db.util.SingletonDB;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutoFormController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Categoria> cbCategoria;

    @FXML
    private ComboBox<Unidade> cbUnidade;

    @FXML
    private TextField tfDescricao;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfPreco;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() { // espera a tela carregar completamente e realiza essa ação
                tfNome.requestFocus();
            }
        });
        List<Categoria> categoriaList= new CategoriaDAL().get("");
        List<Unidade> unidadeList= new UnidadeDAL().get("");
        ObservableList cats= FXCollections.observableArrayList();
        ObservableList unids= FXCollections.observableArrayList();
        for(Categoria categoria:categoriaList){
            cats.addAll(categoria);
        }for(Unidade unidade:unidadeList){
            unids.addAll(unidade);
        }
        cbCategoria.setItems(cats);
        cbUnidade.setItems(unids);
        // Se for alteração, os dados devem ser preenchidos
        if(CategoriaConsultaController.categoria!=null){
            Categoria aux= CategoriaConsultaController.categoria;
            tfNome.setText(aux.getNome());
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        btCancelar.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        Unidade unidade = cbUnidade.getValue();
        Categoria categoria=cbCategoria.getValue();
        Produto produto= new Produto(tfNome.getText(),Double.parseDouble(tfPreco.getText()),tfDescricao.getText(),categoria,unidade);
        if (!new ProdutoDAL().gravar(produto)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erro ao gravar produto "+ SingletonDB.getConexao().getMensagemErro());
            alert.showAndWait();
        }
        btCancelar.getScene().getWindow().hide();
    }

}
