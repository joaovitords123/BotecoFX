package com.example.botecofx.db.dals;

import com.example.botecofx.db.entidades.Comanda;
import com.example.botecofx.db.entidades.Garcom;
import com.example.botecofx.db.entidades.Pagamento;
import com.example.botecofx.db.entidades.TipoPagamento;
import com.example.botecofx.db.util.IDAL;
import com.example.botecofx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAL implements IDAL<Pagamento> {
    @Override
    public boolean gravar(Pagamento entidade) {
        String sql = """
                INSERT INTO pagamento(
                	com_id, pag_valor, tpg_id)
                	VALUES (#1, #2, #3);
                """;
        sql=sql.replace("#1",entidade.getComanda().getId()+"");
        sql = sql.replace("#2",entidade.getValor()+"");
        sql = sql.replace("#3", entidade.getTipoPagamento().getId()+"");
       return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Pagamento entidade) {
        String sql = """
                UPDATE public.pagamento
                	SET com_id=#1, pag_valor=#2, tpg_id=#3
                	WHERE pag_id = #4;
                """;
        sql = sql.replace("#1",entidade.getComanda().getId()+"");
        sql = sql.replace("#2",entidade.getValor()+"");
        sql = sql.replace("#3",entidade.getTipoPagamento().getId()+"");
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Pagamento entidade) {
        return SingletonDB.getConexao().manipular("DELETE FROM pagamento WHERE pag_id = " + entidade.getId()+ ";");
    }

    @Override
    public Pagamento get(int id) {
        Pagamento pag = null;
        String sql = "SELECT * FROM pagamento WHERE pag_id = "+ id+";";
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        ResultSet cs,ps,gs;
        try
        {

            if(rs.next()){
                cs = SingletonDB.getConexao().consultar("SELECT * from comanda WHERE com_id = " + rs.getInt("com_id"));
                ps = SingletonDB.getConexao().consultar("SELECT * from tipopgto WHERE tpg_id = " + rs.getInt("tpg_id"));
                if(cs.next() && ps.next())
                {
                    gs=SingletonDB.getConexao().consultar("SELECT * FROM garcon WHERE gar_id = " + cs.getInt("gar_id"));
                    pag = new Pagamento(rs.getInt("pag_id"), rs.getDouble("pag_valor"),
                            new TipoPagamento(ps.getString("tpg_nome")),
                            new Comanda(cs.getInt("com_numero"),
                            cs.getString("com_desc"),
                            cs.getDate("com_data").toLocalDate(),
                            cs.getInt("com_id"),
                            cs.getString("com_status").charAt(0),
                            new Garcom(gs.getString("gar_nome"), gs.getString("gar_cpf"), gs.getString("gar_cep"),
                            gs.getString("gar_endereco"), gs.getString("gar_numero"), gs.getString("gar_cidade"),
                            gs.getString("gar_uf"), gs.getString("gar_fone"))));
                }

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return pag;
    }

    @Override
    public List<Pagamento> get(String filtro) {
        List<Pagamento> pag = new ArrayList<>();
        String sql = "SELECT * FROM pagamento";
        if(!filtro.isEmpty())
            sql+= " WHERE " + filtro;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        ResultSet cs,ps,gs;
        try
        {
            while(rs.next()){
                cs = SingletonDB.getConexao().consultar("SELECT * from comanda WHERE com_id = " + rs.getInt("com_id"));
                ps = SingletonDB.getConexao().consultar("SELECT * from tipopgto WHERE tpg_id = " + rs.getInt("tpg_id"));
                if(cs.next() && ps.next())
                {
                    gs=SingletonDB.getConexao().consultar("SELECT * FROM garcon WHERE gar_id = " + cs.getInt("gar_id"));
                    if(gs.next()) {
                        pag.add(new Pagamento(rs.getInt("pag_id"), rs.getDouble("pag_valor"),
                                new TipoPagamento(ps.getString("tpg_nome")),
                                new Comanda(cs.getInt("com_numero"),
                                        cs.getString("com_desc"),
                                        cs.getDate("com_data").toLocalDate(),
                                        cs.getInt("com_id"),
                                        cs.getString("com_status").charAt(0),
                                        new Garcom(gs.getString("gar_nome"), gs.getString("gar_cpf"), gs.getString("gar_cep"),
                                                gs.getString("gar_endereco"), gs.getString("gar_numero"), gs.getString("gar_cidade"),
                                                gs.getString("gar_uf"), gs.getString("gar_fone")))));
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return pag;
    }
}
