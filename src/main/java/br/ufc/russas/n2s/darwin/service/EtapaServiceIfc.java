/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.service;

import br.ufc.russas.n2s.darwin.beans.AvaliacaoBeans;
import br.ufc.russas.n2s.darwin.beans.DocumentacaoBeans;
import br.ufc.russas.n2s.darwin.beans.EtapaBeans;
import br.ufc.russas.n2s.darwin.beans.ParticipanteBeans;
import br.ufc.russas.n2s.darwin.beans.SelecaoBeans;
import br.ufc.russas.n2s.darwin.beans.UsuarioBeans;
import java.util.List;

/**
 *
 * @author Wallison Carlos
 */
public interface EtapaServiceIfc extends ServiceIfc{
    EtapaBeans adicionaEtapa(SelecaoBeans selecao, EtapaBeans etapa) throws IllegalAccessException;
    EtapaBeans atualizaEtapa(SelecaoBeans selecao, EtapaBeans etapa) throws IllegalAccessException;
    void removeEtapa(EtapaBeans etapa);
    List<EtapaBeans> listaTodasEtapas();
    EtapaBeans getEtapa(long codEtapa);
    boolean isParticipante(ParticipanteBeans participante);
    boolean isParticipante(UsuarioBeans participante);
    ParticipanteBeans getParticipante(EtapaBeans etapa, UsuarioBeans usuario);
    void anexaDocumentacao(EtapaBeans etapa, DocumentacaoBeans documentacao) throws IllegalAccessException;
    void avalia(EtapaBeans etapa, AvaliacaoBeans avaliacao) throws IllegalAccessException;
    List<Object[]> getParticipantes(EtapaBeans etapa);
    List<Object[]> getAprovados(EtapaBeans etapa);
    SelecaoBeans getSelecao(EtapaBeans etapa);
    void participa(EtapaBeans inscricao, ParticipanteBeans participante) throws IllegalAccessException;
    void participa(EtapaBeans inscricao, ParticipanteBeans participante, DocumentacaoBeans documentacao) throws IllegalAccessException; 
    Object[] getSituacao(EtapaBeans etapa, UsuarioBeans usuario);
    List<EtapaBeans> ordenaEtapasPorData(List<EtapaBeans> etapas);
}
