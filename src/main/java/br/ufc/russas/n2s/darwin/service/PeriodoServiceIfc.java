/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.service;

import br.ufc.russas.n2s.darwin.beans.PeriodoBeans;
import java.util.List;

/**
 *
 * @author Wallison Carlos
 */
public interface PeriodoServiceIfc {
    public void adicionaPeriodo(PeriodoBeans periodo);
    public void atualizaPeriodo(PeriodoBeans periodo);
    public void removePeriodo(PeriodoBeans periodo);
    public List<PeriodoBeans> listaTodosPeriodos();
    public PeriodoBeans getPeriodo(long codPeriodo);
}
