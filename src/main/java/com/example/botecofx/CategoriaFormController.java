package com.example.botecofx;

import com.example.botecofx.db.dals.CategoriaDAL;
import com.example.botecofx.db.dals.UnidadeDAL;
import com.example.botecofx.db.entidades.Categoria;
import com.example.botecofx.db.entidades.Unidade;
import com.example.botecofx.db.util.SingletonDB;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriaFormController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() { // espera a tela carregar completamente e realiza essa ação
                tfNome.requestFocus();
            }
        });
        // Se for alteração, os dados devem ser preenchidos
        if(CategoriaConsultaController.categoria!=null){
            Categoria aux= CategoriaConsultaController.categoria;
            tfNome.setText(aux.getNome());
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        tfNome.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        Categoria categoria= new Categoria(tfNome.getText());
        if (!new CategoriaDAL().gravar(categoria)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erro ao gravar categoria "+ SingletonDB.getConexao().getMensagemErro());
            alert.showAndWait();
        }
        btCancelar.getScene().getWindow().hide();
    }

}
