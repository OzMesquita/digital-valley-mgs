/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.model;

import javax.persistence.Entity;

/**
 *
 * @author Lavínia Matoso
 */
@Entity
public enum EnumCriterioDeAvaliacao {
    NOTA(1),
    APROVACAO(2),
    DEFERIMENTO(3);
    
    private int criterio;
    
    EnumCriterioDeAvaliacao() {
    
    }
    
    EnumCriterioDeAvaliacao(int criterio) {
        setCriterio(criterio);
    }
    
    public int getCriterio(){
        return criterio;
    }
    
    public void setCriterio(int criterio){
        if (criterio>=1 && criterio<=3) { 
            this.criterio = criterio;
        } else {
            throw new IllegalArgumentException("Critério de avaliação deve ser maior igual a um e menor igual a três!");
        }            
    }
}
