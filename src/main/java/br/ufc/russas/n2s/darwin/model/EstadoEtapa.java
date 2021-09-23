/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.model;

import javax.persistence.Embeddable;

/**
 *
 * @author Wallison Carlos
 */
@Embeddable
public interface EstadoEtapa {
    EnumEstadoEtapa execute(Etapa etapa);
}
