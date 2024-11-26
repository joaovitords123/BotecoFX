package com.example.botecofx.db.entidades;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private String descr;
    private Categoria categoria;
    private Unidade unidade;

    public Produto(int id, String nome, double preco, String descr, Categoria categoria, Unidade unidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descr = descr;
        this.categoria = categoria;
        this.unidade = unidade;
    }

    public Produto(String nome, double preco, String descr, Categoria categoria, Unidade unidade) {
        this(0, nome, preco, descr, categoria, unidade);
    }

    public Produto() {
        this(0,"",0,"",null,null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    @Override
    public String toString() {
        return nome;
    }
}
