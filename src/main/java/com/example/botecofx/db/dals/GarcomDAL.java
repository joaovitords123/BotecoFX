package com.example.botecofx.db.dals;

import com.example.botecofx.db.entidades.Garcom;
import com.example.botecofx.db.entidades.Unidade;
import com.example.botecofx.db.util.IDAL;
import com.example.botecofx.db.util.SingletonDB;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GarcomDAL implements IDAL <Garcom> {

    public boolean gravar(Garcom entidade, File foto) {
        String sql = """
                    INSERT INTO garcon(gar_nome, gar_cpf, gar_cep, gar_endereco, gar_cidade, gar_uf, gar_fone, gar_numero) VALUES ('#1','#2','#3','#4','#5','#6','#7', '#8')
                """;
        sql = sql.replace("#1", entidade.getNome());
        sql = sql.replace("#2", entidade.getCpf());
        sql = sql.replace("#3", entidade.getCep());
        sql = sql.replace("#4", entidade.getEndereco());
        sql = sql.replace("#5", entidade.getCidade());
        sql = sql.replace("#6", entidade.getUf());
        sql = sql.replace("#7", entidade.getFone());
        sql = sql.replace("#8", entidade.getNumero());

        if(SingletonDB.getConexao().manipular(sql)) {
            if(foto!=null) {
                int id = SingletonDB.getConexao().getMaxPK("garcon", "gar_id");
                if(SingletonDB.getConexao().gravarImagem(foto,"garcon", "gar_foto", "gar_id", id)) {
                    return true;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean gravar(Garcom entidade) {
        return false;
    }

    @Override
    public boolean alterar(Garcom entidade) {
        String sql = """
                    UPDATE garcon SET gar_nome = '#1'
                                      gar_cpf = '#2'
                                      gar_cep = '#3'
                                      gar_endereco = '#4'
                                      gar_cidade = '#5'
                                      gar_uf = '#6'
                                      gar_fone = '#7'
                                    WHERE gar_id = '#8'
                """;
        sql = sql.replace("#1", entidade.getNome());
        sql = sql.replace("#2", entidade.getCpf());
        sql = sql.replace("#3", entidade.getCep());
        sql = sql.replace("#4", entidade.getEndereco());
        sql = sql.replace("#5", entidade.getCidade());
        sql = sql.replace("#6", entidade.getUf());
        sql = sql.replace("#7", entidade.getFone());
        sql = sql.replace("#8", ""+entidade.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Garcom entidade) {
        return SingletonDB.getConexao().manipular("DELETE FROM garcon WHERE gar_id = " + entidade.getId());
    }

    @Override
    public Garcom get(int id) {
        Garcom gar = null;
        String sql = "SELECT * FROM garcon WHERE gar_id = "+ id;
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try {
            if(resultSet.next()) {
                gar = new Garcom(resultSet.getInt("gar_id"), resultSet.getString("gar_nome"),
                                 resultSet.getString("gar_cpf"),
                                 resultSet.getString("gar_cep"),
                                 resultSet.getString("gar_endereco"),
                                 "",
                                 resultSet.getString("gar_cidade"),
                                 resultSet.getString("gar_uf"),
                                 resultSet.getString("gar_fone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gar;
    }

    @Override
    public List<Garcom> get(String filtro) {
        List<Garcom> gar = new ArrayList<>();
        String sql = "SELECT * FROM garcon";
        if(!filtro.isEmpty()) {
            sql += "WHERE " + filtro;
        }
        sql += " ORDER BY gar_nome";
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try {
            while(resultSet.next()) {
                gar.add(new Garcom(resultSet.getInt("gar_id"), resultSet.getString("gar_nome"),
                        resultSet.getString("gar_cpf"),
                        resultSet.getString("gar_cep"),
                        resultSet.getString("gar_endereco"),
                        "",
                        resultSet.getString("gar_cidade"),
                        resultSet.getString("gar_uf"),
                        resultSet.getString("gar_fone")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gar;
    }
}
