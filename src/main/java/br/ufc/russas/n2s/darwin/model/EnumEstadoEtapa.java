/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.model;

import java.time.LocalDate;

/**
 *
 * @author Wallison Carlos
 */
public enum EnumEstadoEtapa implements EstadoEtapa{
    
    ESPERA(1){
        @Override
        public EnumEstadoEtapa execute(Etapa etapa){
            if(etapa.getPeriodo().getInicio().isAfter(LocalDate.now())){
                return this;  
            }else{
                return ANDAMENTO.execute(etapa);
            }
        }
    },
    ANDAMENTO(2){
        @Override
        public EnumEstadoEtapa execute(Etapa etapa){
            if((etapa.getPeriodo().getInicio().isBefore(LocalDate.now()) || etapa.getPeriodo().getInicio().isEqual(LocalDate.now()))
                    && (etapa.getPeriodo().getTermino().isAfter(LocalDate.now())) || etapa.getPeriodo().getTermino().equals(LocalDate.now())){
                etapa.listParticipantes();
            	return this;
            }else{
                return FINALIZADA.execute(etapa);
            }
        }
    },
    FINALIZADA(3){
        @Override
        public EnumEstadoEtapa execute(Etapa etapa){
            if(etapa.getPeriodo().getTermino().isBefore(LocalDate.now())){
            	etapa.listParticipantes();
                return this;
            }else{
                return ESPERA.execute(etapa);
            }
        }
    };
    
    private int estado;
    EnumEstadoEtapa() {
        
    }
    
    EnumEstadoEtapa(int estado) {
        setEstado(estado);
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        if (estado >= 1 && estado <= 3) {
            this.estado = estado;
        } else {
            throw new IllegalArgumentException("Critério de avaliação deve ser maior igual a um e menor igual a três!");
        }
    }
    
    
}
