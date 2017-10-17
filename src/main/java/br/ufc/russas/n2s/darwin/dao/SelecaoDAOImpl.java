/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.dao;

import br.ufc.russas.n2s.darwin.model.Selecao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author N2S-PC03
 */
@Repository("selecaoDAOIfc")
@Transactional
public class SelecaoDAOImpl implements SelecaoDAOIfc{

    private DAOIfc<Selecao> daoImpl;
    
    @Autowired
    public void setDAOIfc(@Qualifier("daoImpl")DAOIfc<Selecao> dao){
        this.daoImpl = dao;
    }
    
    @Override
    public void adicionaSelecao(Selecao selecao) {
        this.daoImpl.adiciona(selecao);
    }

    @Override
    public void atualizaSelecao(Selecao selecao) {
        this.daoImpl.atualiza(selecao);
    }

    @Override
    public void removeSelecao(Selecao selecao) {
        this.daoImpl.remove(selecao);
    }

    @Override
    public List<Selecao> listaSelecoes() {
        return this.daoImpl.lista(Selecao.class);
    }

    @Override
    public Selecao getSelecao(long codigo) {
        return this.daoImpl.getObject(Selecao.class, codigo);
    }
      
}
