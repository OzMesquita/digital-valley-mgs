/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.service;

import br.ufc.russas.n2s.darwin.beans.PeriodoBeans;
import br.ufc.russas.n2s.darwin.dao.PeriodoDAOIfc;
import br.ufc.russas.n2s.darwin.model.Periodo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Wallison Carlos
 */
public class PeriodoServiceImpl implements PeriodoServiceIfc {

    private PeriodoDAOIfc periodoDAOIfc;

    public PeriodoDAOIfc getPeriodoDAOIfc() {
        return periodoDAOIfc;
    }
    @Autowired(required = true)
    public void setPeriodoDAOIfc(@Qualifier("periodoDAOIfc")PeriodoDAOIfc periodoDAOIfc) {
        this.periodoDAOIfc = periodoDAOIfc;
    }

    @Override
    public void adicionaPeriodo(PeriodoBeans periodo) {
        this.getPeriodoDAOIfc().adicionaPeriodo((Periodo) periodo.toBusiness());
    }

    @Override
    public void atualizaPeriodo(PeriodoBeans periodo) {
        this.getPeriodoDAOIfc().atualizaPeriodo((Periodo) periodo.toBusiness());
    }

    @Override
    public void removePeriodo(PeriodoBeans periodo) {
        this.getPeriodoDAOIfc().removePeriodo((Periodo) periodo.toBusiness());
    }

    @Override
    public List<PeriodoBeans> listaTodosPeriodos() {
        Periodo p = new Periodo();
        List<PeriodoBeans> periodos = Collections.synchronizedList(new ArrayList<PeriodoBeans>());
        List<Periodo> result = this.getPeriodoDAOIfc().listaPeriodos(p);
        for (Periodo periodo : result) {
            periodos.add((PeriodoBeans) new PeriodoBeans().toBeans(periodo));
        }
        return periodos;
    }

    @Override
    public PeriodoBeans getPeriodo(long codPeriodo) {
        Periodo p = new Periodo();
        p.setCodPeriodo(codPeriodo);
        return (PeriodoBeans) new PeriodoBeans().toBeans(this.getPeriodoDAOIfc().getPeriodo(p));
    }

}
