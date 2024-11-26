package com.example.botecofx.db.entidades;

public class Pagamento {
    private int id;
    private double valor;
    private TipoPagamento tipoPagamento;
    private Comanda comanda;

    public Pagamento(int id, double valor, TipoPagamento tipoPagamento, Comanda comanda) {
        this.id = id;
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
        this.comanda = comanda;
    }

    public Pagamento(double valor, TipoPagamento tipoPagamento, Comanda comanda) {
        this(0, valor, tipoPagamento, comanda);
    }

    public Pagamento() {
        this(0,0,null,null);
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

}
