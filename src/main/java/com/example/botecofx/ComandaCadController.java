package com.example.botecofx;

import com.example.botecofx.db.dals.ComandaDAL;
import com.example.botecofx.db.dals.GarcomDAL;
import com.example.botecofx.db.entidades.Comanda;
import com.example.botecofx.db.entidades.Garcom;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class ComandaCadController implements Initializable {
    public ComboBox<Garcom> cbGarcom;
    public TextField tfNumComanda;
    public DatePicker dtCommand;
    public TextArea tfDescr;

    public boolean buscaComanda(int numComanda) {
        List<Comanda> comandas = new ComandaDAL().get("");
        for(Comanda com:comandas) {
            if(com.getNumero() == numComanda)
                return false;
        }
        return true;
    }

    public void onConfirmarCad(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Criar Comanda");
        alert.setHeaderText("Deseja criar a comanda "+tfNumComanda.getText()+"?");
        alert.setContentText("Cadastro de comanda");
        Optional<ButtonType> res = alert.showAndWait();
        if(res.get() == ButtonType.OK) {
            int numCom = Integer.parseInt(tfNumComanda.getText());
            if(buscaComanda(numCom)) {
                Garcom gar = cbGarcom.getValue();
                int idComanda = numCom;
                Comanda comanda = new Comanda(0, idComanda, tfDescr.getText(), dtCommand.getValue(), 0, 'A', gar);
                new ComandaDAL().gravar(comanda);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
            else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setHeaderText("Numero de comanda ja existente!");
                alert1.showAndWait();
            }
        }
    }

    public void onCancelarCad(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbGarcom.getItems().addAll(new GarcomDAL().get(""));
    }
}
