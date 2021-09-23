/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.model;

/**
 *
 * @author Wallison Carlos
 */
public enum EnumEstadoAvaliacao {
    
    AVALIADO(1),
    PENDENTE(2);    

    private int nivel;
    
    EnumEstadoAvaliacao() {
        
    }

    EnumEstadoAvaliacao(int nivel) {
        setNivel(nivel);
    }

    public int getNivel() {
        return nivel;
    }
    
    public void setNivel(int nivel){
        if (nivel>=1 && nivel<=2) { 
            this.nivel = nivel;
        } else {
            throw new IllegalArgumentException("Estado de avaliação deve ser maior igual a um e menor igual a dois!");
        }            
    }
    
}
