package com.example.botecofx.db.entidades;

public class Garcom {
    private int id;
    private String nome;
    private String cpf;
    private String cep;
    private String endereco;
    private String numero;
    private String cidade;
    private String uf;
    private String fone;

    public Garcom(int id, String nome, String cpf, String cep, String endereco, String numero, String cidade, String uf, String fone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
        this.endereco = endereco;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
        this.fone = fone;
    }

    public Garcom(String nome, String cpf, String cep, String endereco, String numero, String cidade, String uf, String fone) {
        this(0,nome,cpf,cep,endereco,numero,cidade,uf,fone);
    }

    public Garcom() {
        this(0,"","","","","","","","");
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    @Override
    public String toString() {
        return nome;
    }
}
