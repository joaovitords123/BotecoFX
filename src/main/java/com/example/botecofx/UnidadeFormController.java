package com.example.botecofx;

import com.example.botecofx.db.dals.UnidadeDAL;
import com.example.botecofx.db.entidades.TipoPagamento;
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

public class UnidadeFormController implements Initializable {

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
        if(UnidadeConsultaController.unidade!=null){
            Unidade aux= UnidadeConsultaController.unidade;
            tfNome.setText(aux.getNome());
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        btConfirmar.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        Unidade unidade= new Unidade(tfNome.getText());
        if (!new UnidadeDAL().gravar(unidade)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erro ao gravar unidade "+ SingletonDB.getConexao().getMensagemErro());
            alert.showAndWait();
        }
        btCancelar.getScene().getWindow().hide();
    }
}
