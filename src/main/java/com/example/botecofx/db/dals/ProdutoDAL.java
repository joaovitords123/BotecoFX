package com.example.botecofx.db.dals;

import com.example.botecofx.db.entidades.Produto;
import com.example.botecofx.db.util.IDAL;
import com.example.botecofx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAL implements IDAL<Produto> {
    @Override
    public boolean gravar(Produto entidade) {
        String sql = """
                    INSERT INTO produto(
                        cat_id, uni_id, prod_nome, prod_preco, prod_descr
                    )
                    VALUES (#1, #2, '#3', #4, '#5');
                """;
        sql = sql.replace("#1", ""+entidade.getCategoria().getId());
        sql = sql.replace("#2", ""+entidade.getUnidade().getId());
        sql = sql.replace("#3", entidade.getNome());
        sql = sql.replace("#4", ""+entidade.getPreco());
        sql = sql.replace("#5", entidade.getDescr());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Produto entidade) {
        String sql = """
                    UPDATE produto SET
                        cat_id = #1, uni_id = #2, prod_nome = '#3', prod_preco = #4, prod_descr = '#5'
                    WHERE prod_id = #6
                """;
        sql = sql.replace("#1", ""+entidade.getCategoria().getId());
        sql = sql.replace("#2", ""+entidade.getUnidade().getId());
        sql = sql.replace("#3", entidade.getNome());
        sql = sql.replace("#4", ""+entidade.getPreco());
        sql = sql.replace("#5", entidade.getDescr());
        sql = sql.replace("#6", ""+entidade.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Produto entidade) {
        return SingletonDB.getConexao().manipular("DELETE FROM produto WHERE prod_id = " + entidade.getId());
    }

    @Override
    public Produto get(int id) {
        Produto produto = null;
        String sql = "SELECT * FROM produto WHERE prod_id = " + id;
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try {
            if(resultSet.next()) {
                produto = new Produto(resultSet.getInt("prod_id"),
                        resultSet.getString("prod_nome"),
                        resultSet.getDouble("prod_preco"),
                        resultSet.getString("prod_descr"),
                        new CategoriaDAL().get(resultSet.getInt("cat_id")),
                        new UnidadeDAL().get(resultSet.getInt("uni_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produto;
    }

    @Override
    public List<Produto> get(String filtro) {
        List <Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto ";
        if (!filtro.isEmpty()) {
            sql += "WHERE " + filtro;
        }
        sql += " ORDER BY prod_nome";
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try {
            while(resultSet.next()) {
                produtos.add(
                        new Produto(resultSet.getInt("prod_id"),
                        resultSet.getString("prod_nome"),
                        resultSet.getDouble("prod_preco"),
                        resultSet.getString("prod_descr"),
                        new CategoriaDAL().get(resultSet.getInt("cat_id")),
                        new UnidadeDAL().get(resultSet.getInt("uni_id")))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }
}
