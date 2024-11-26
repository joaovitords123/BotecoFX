package com.example.botecofx.db.dals;

import com.example.botecofx.db.entidades.TipoPagamento;
import com.example.botecofx.db.entidades.Unidade;
import com.example.botecofx.db.util.IDAL;
import com.example.botecofx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UnidadeDAL implements IDAL <Unidade> {
    @Override
    public boolean gravar(Unidade entidade) {
        String sql = """
                    INSERT INTO unidade(uni_nome) VALUES ('#1')
                """;
        sql = sql.replace("#1", entidade.getNome());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Unidade entidade) {
        String sql = """
                    UPDATE unidade SET uni_nome = '#1'
                                    WHERE uni_id = '#2'
                """;
        sql = sql.replace("#1", entidade.getNome());
        sql = sql.replace("#2", ""+entidade.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Unidade entidade) {
        return SingletonDB.getConexao().manipular("DELETE FROM unidade WHERE uni_id = " + entidade.getId());
    }

    @Override
    public Unidade get(int id) {
        Unidade uni = null;
        String sql = "SELECT * FROM unidade WHERE uni_id = "+ id;
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try {
            if(resultSet.next()) {
                uni = new Unidade(resultSet.getInt("uni_id"), resultSet.getString("uni_nome"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uni;
    }

    @Override
    public List<Unidade> get(String filtro) {
        List <Unidade> unidades = new ArrayList<>();
        String sql = "SELECT * FROM unidade ";
        if(!filtro.isEmpty()) {
            sql += "WHERE " + filtro;
        }
        sql += " ORDER BY unidade";
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try {
            while(resultSet.next()) {
                unidades.add(new Unidade(resultSet.getInt("uni_id"), resultSet.getString("uni_nome")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unidades;
    }
}
