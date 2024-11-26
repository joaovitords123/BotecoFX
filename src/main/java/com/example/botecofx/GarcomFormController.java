package com.example.botecofx;

import com.example.botecofx.db.dals.GarcomDAL;
import com.example.botecofx.db.entidades.Garcom;
import com.example.botecofx.db.util.SingletonDB;
import com.example.botecofx.util.ApisService;
import com.example.botecofx.util.MaskFieldUtil;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GarcomFormController implements Initializable {
    public ImageView imageView;
    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfCep;

    @FXML
    private TextField tfCidade;

    @FXML
    private TextField tfCpf;

    @FXML
    private TextField tfEnd;

    @FXML
    private TextField tfFone;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfNumero;

    @FXML
    private TextField tfUf;

    File file = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tfNome.requestFocus();
            }
        });
        MaskFieldUtil.cpfField(tfCpf);
        MaskFieldUtil.cepField(tfCep);
        MaskFieldUtil.foneField(tfFone);
        //se for uma alteração
        if(GarcomConsultaController.garcom!=null) {
            Garcom aux = GarcomConsultaController.garcom;
            tfNome.setText(aux.getNome());
            BufferedImage bufferedImage;
            int id = SingletonDB.getConexao().getMaxPK("garcon", "gar_id");
            bufferedImage = SingletonDB.getConexao().carregarImagem("garcon", "gar_foto", "gar_id", id);
            if(bufferedImage!=null)
                imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
            tfCep.setText(GarcomConsultaController.garcom.getCep());
            tfEnd.setText(aux.getEndereco());
            tfUf.setText(aux.getUf());
            tfCidade.setText(aux.getCidade());
            tfFone.setText(aux.getFone());
            tfCpf.setText(aux.getCpf());
            tfNumero.setText(aux.getNumero());
            tfId.setText(aux.getId()+"");
        }
        //preencher os dados do garçom

    }

    @FXML
    void onCancelar(ActionEvent event) {
        btCancelar.getScene().getWindow().hide();;
    }

    @FXML
    void onCofirmar(ActionEvent event) {
        Garcom garcom = new Garcom(tfNome.getText(), tfCpf.getText(), tfCep.getText(), tfEnd.getText()
                                   ,tfNumero.getText(), tfCidade.getText(), tfUf.getText(), tfFone.getText());
        if(!new GarcomDAL().gravar(garcom, file)) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erro ao gravar garcom: "+ SingletonDB.getConexao().getMensagemErro());
            alert.showAndWait();
        }
        btConfirmar.getScene().getWindow().hide();
    }

    @FXML
    void tfNome(ActionEvent event) {

    }

    public void onBuscarCep(KeyEvent keyEvent) {
        if(tfCep.getText().length() == 9) {
            String dados = ApisService.consultaCep(tfCep.getText(), "json");
            JSONObject json = new JSONObject(dados);
            tfCidade.setText(json.getString("localidade"));
            tfEnd.setText(json.getString("logradouro"));
            tfUf.setText(json.getString("uf"));
        }
    }

    public void onBuscarImagem(MouseEvent mouseEvent) {
        // Aceitar somente imagem png e jpeg
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        if(file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);

        }
    }
}
