package com.example.botecofx;

import com.example.botecofx.db.dals.*;
import com.example.botecofx.db.entidades.*;
import com.example.botecofx.db.util.SingletonDB;
import com.example.botecofx.util.ModalTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ComandaFormController {

    public Button btProduto;
    @FXML
    private ComboBox<Garcom> cbGarcom;

    @FXML
    private ComboBox<TipoPagamento> cbTipoPagamento;

    @FXML
    private TableColumn<Comanda.Item, String> colProduto;

    @FXML
    private TableColumn<Comanda.Item, String> colQuant;

    @FXML
    private TableColumn<Comanda.Item, String> colValor;

    @FXML
    private DatePicker dpData;

    @FXML
    private Label lbValor;

    @FXML
    private Spinner<Integer> spQuant;

    @FXML
    private TextArea taDesc;

    @FXML
    private TableView<Comanda.Item> tableView;

    @FXML
    private TextField tfNumero;

    private Comanda comanda;

    private ObservableList<Comanda.Item> itemLista = FXCollections.observableArrayList();

    Produto produto = null;

    double valorPago = 0;

    @FXML
    void onAdd(ActionEvent event) {
        if(produto != null && spQuant.getValue() != 0) {
            if(comanda.getStatus() == 'A') {
                Comanda.Item item = new Comanda.Item(produto, spQuant.getValue());
                boolean itemExiste = false;
                for (int i = 0; i < itemLista.size(); i++) {
                    if (tableView.getItems().get(i).produto().getId() == produto.getId()) {
                        Comanda.Item itemAtt = tableView.getItems().get(i);
                        itemLista.set(i, new Comanda.Item(produto, itemAtt.quant() + spQuant.getValue()));
                        itemExiste = true;
                    }
                }
                if (!itemExiste) {
                    itemLista.add(item);
                }
                spQuant.getValueFactory().setValue(0);
                setValorComanda();
            }
            else {
                JOptionPane.showMessageDialog(null, "Erro ao incluir produto: comanda finalizada!");
            }
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Nenhuma alteração foi realizada!");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        if(comanda.getStatus()!='F') {
            ComandaDAL cmdDal = new ComandaDAL();
            comanda.delItens();
            for (Comanda.Item item : itemLista) {
                comanda.addItem(item);
            }
            cmdDal.alterar(comanda);
        }
    }

    @FXML
    void onFinalizar(ActionEvent event) {
        if(comanda.getStatus()!='F') {
            double val;
            val = Double.parseDouble(lbValor.getText());
            if (val-valorPago == 0) {
                comanda.setStatus('F');
                JOptionPane.showMessageDialog(null, "Comanda numero: "+comanda.getNumero()+" fechada!");
                ActionEvent ev = null;
                onConfirmar(ev);
            } else {
                JOptionPane.showMessageDialog(null, "Realize o pagamento da comanda para finaliza-la!");
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Comanda ja finalizada!");
        }
    }

    @FXML
    void onImprimir(ActionEvent event) {
        int comandaId = ComandaController.id;
        HashMap hashMap = new HashMap();
        hashMap.put("comanda_id", comandaId);
        gerarRelatorioSubReport("reports/comanda_print.jasper", "Comanda", hashMap);
    }

    @FXML
    void onSelProduto(ActionEvent event) {
        ModalTable modalTable = new ModalTable(new ProdutoDAL().get(""),
                                new String[]{"nome", "preco", "categoria"},
                                "nome");
        Stage stage=new Stage();
        stage.setScene(new Scene(modalTable));
        stage.setWidth(600); stage.setHeight(480); stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        produto = (Produto)modalTable.getSelecionado();
        btProduto.setText(produto.toString());
    }

    private void gerarRelatorioSubReport(String relat, String titulotela, Map parametros)
    {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(relat, parametros,
                    SingletonDB.getConexao().getConnect());
            JasperViewer viewer = new JasperViewer(jasperPrint,false);

            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);//maximizado
            viewer.setTitle(titulotela);
            viewer.setVisible(true);
        } catch (JRException erro) {
            System.out.println(erro);
        }
    }

    public void alterSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        spQuant.setValueFactory(valueFactory);
        spQuant.valueProperty().addListener((obs, oldValue, newValue) -> {
        });
    }

    public void setValorComanda() {
        double valor = 0;
        for(Comanda.Item val:itemLista) {
            valor += val.quant()*val.produto().getPreco();
        }
        if(valor < 0) {
            valor = 0;
        }
        lbValor.setText(""+valor);
        comanda.setValor(valor - valorPago);
    }

    public void carregarComanda(int comandaId) {
        comanda = new ComandaDAL().get(comandaId);
        Garcom garcom = new GarcomDAL().get(comanda.getGarcom().getId());
        cbGarcom.getItems().add(garcom);
        cbGarcom.setValue(garcom);
        // Define o intervalo (mínimo, máximo e valor inicial) do Spinner
        alterSpinner();
        colProduto.setCellValueFactory(valCol -> new SimpleStringProperty(valCol.getValue().produto().getNome()));
        colQuant.setCellValueFactory(valCol -> new SimpleStringProperty(""+valCol.getValue().quant()));
        colValor.setCellValueFactory(valCol -> new SimpleStringProperty(""+valCol.getValue().quant() * valCol.getValue().produto().getPreco()));
        itemLista.addAll(comanda.getItens());
        tableView.setItems(itemLista);
        cbTipoPagamento.getItems().addAll(new TipoPagamentoDAL().get(""));
        dpData.setValue(comanda.getData());
        tfNumero.setText(comanda.getNumero() + "");
        taDesc.setText(comanda.getDescricao());
        carregarPagamentos();
        setValorComanda();
    }

    public void carregarPagamentos() {
        valorPago = 0;
        List<Pagamento>totalPag = new PagamentoDAL().get("com_id = "+comanda.getId());
        for(Pagamento pagamento1:totalPag) {
            valorPago+= pagamento1.getValor();
        }
        setValorComanda();
    }

    public void onPagar(ActionEvent actionEvent) {
        if(comanda.getStatus()!='F') {
            if (cbTipoPagamento.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Selecione uma forma de pagamento");
                alert.showAndWait();
            } else {
                String pagamento = JOptionPane.showInputDialog(null, "Informe o valor do pagamento:");
                if (pagamento != null) {
                    double valor = Double.parseDouble(pagamento);
                    if(valor>comanda.getValor()-valorPago) {
                        valor = comanda.getValor()-valorPago;
                        Pagamento pag = new Pagamento(valor, cbTipoPagamento.getValue(), comanda);
                        new PagamentoDAL().gravar(pag);
                    }
                    else {
                        Pagamento pag = new Pagamento(valor, cbTipoPagamento.getValue(), comanda);
                        new PagamentoDAL().gravar(pag);
                    }
                    carregarPagamentos();
                    ActionEvent ev = null;
                    onConfirmar(ev);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Pagamento cancelado!");
                    alert.showAndWait();
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Não foi possivel realizar o pagamento: comanda já está fechada!");
        }
    }
}