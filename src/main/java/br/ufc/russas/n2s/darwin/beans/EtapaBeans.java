/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.beans;

import br.ufc.russas.n2s.darwin.model.Avaliacao;
import br.ufc.russas.n2s.darwin.model.EnumCriterioDeAvaliacao;
import br.ufc.russas.n2s.darwin.model.Documentacao;
import br.ufc.russas.n2s.darwin.model.EnumEstadoEtapa;
import br.ufc.russas.n2s.darwin.model.Etapa;
import br.ufc.russas.n2s.darwin.model.Participante;
import br.ufc.russas.n2s.darwin.model.Periodo;
import br.ufc.russas.n2s.darwin.model.UsuarioDarwin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Wallison Carlos, Gilberto
 */
public class EtapaBeans implements Beans {


    private long codEtapa;
    private String titulo;
    private PeriodoBeans periodo;
    private String descricao;
    private List<UsuarioBeans> avaliadores;
    private List<String> documentacaoExigida;
    private EnumCriterioDeAvaliacao criterioDeAvaliacao;
    private List<AvaliacaoBeans> avaliacoes;
    private List<DocumentacaoBeans> documentacoes;
    private EnumEstadoEtapa estado;
    private EtapaBeans prerequisito;
    private float notaMinima;
    private int limiteClassificados;
    private boolean divulgadoResultado;
    private List<ParticipanteBeans> participantes;
    
		    
    public EtapaBeans(){}

    public long getCodEtapa() {
        return codEtapa;
    }

    public void setCodEtapa(long codEtapa) {
        this.codEtapa = codEtapa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public PeriodoBeans getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoBeans periodo) {
        this.periodo = periodo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<UsuarioBeans> getAvaliadores() {
        return avaliadores;
    }

    public void setAvaliadores(List<UsuarioBeans> avaliadores) {
        this.avaliadores = avaliadores;
    }

    public List<String> getDocumentacaoExigida() {
        return documentacaoExigida;
    }

    public void setDocumentacaoExigida(List<String> documentacao) {
        this.documentacaoExigida = documentacao;
    }

    public EnumCriterioDeAvaliacao getCriterioDeAvaliacao() {
        return criterioDeAvaliacao;
    }

    public void setCriterioDeAvaliacao(EnumCriterioDeAvaliacao criterioDeAvaliacao) {
        this.criterioDeAvaliacao = criterioDeAvaliacao;
    }

    public List<AvaliacaoBeans> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoBeans> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public List<DocumentacaoBeans> getDocumentacoes() {
        return documentacoes;
    }

    public void setDocumentacoes(List<DocumentacaoBeans> documentacoes) {
        this.documentacoes = documentacoes;
    }

    public EnumEstadoEtapa getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoEtapa estado) {
        this.estado = estado;
    }

    public EtapaBeans getPrerequisito() {
        return prerequisito;
    }

    public void setPrerequisito(EtapaBeans prerequisito) {
        this.prerequisito = prerequisito;
    }

    public float getNotaMinima() {
        return notaMinima;
    }

    public void setNotaMinima(float notaMinima) {
        this.notaMinima = notaMinima;
    }

    public int getLimiteClassificados() {
        return limiteClassificados;
    }

    public void setLimiteClassificados(int limiteClassificados) {
        this.limiteClassificados = limiteClassificados;
    }

    public boolean isDivulgadoResultado() {
        return divulgadoResultado;
    }

    public void setDivulgaResultado(boolean divulgadoResultado) {
        this.divulgadoResultado = divulgadoResultado;
    }
    
    public List<ParticipanteBeans> getParticipantes() {
    	return this.participantes;
    }
    
    public void setParticipantes(List<ParticipanteBeans> participantes) {
		this.participantes = participantes;
	}
    

    
    @Override
    public Object toBusiness() {
        Etapa etapa = new Etapa();
        if (this.getCodEtapa() > 0) {
            etapa.setCodEtapa(this.getCodEtapa());
        }
        etapa.setTitulo(this.getTitulo());
        etapa.setDescricao(this.getDescricao());
        etapa.setDocumentacaoExigida(this.getDocumentacaoExigida());
        etapa.setCriterioDeAvaliacao(this.getCriterioDeAvaliacao());
        etapa.setEstado(this.getEstado());
        etapa.setPeriodo((Periodo) this.getPeriodo().toBusiness());
        if (this.getPrerequisito() != null) {
        	Etapa prerequisito = (Etapa) this.getPrerequisito().toBusiness();
    		etapa.setPrerequisito(prerequisito);
    	}
        List<UsuarioDarwin> avaliadores = Collections.synchronizedList(new ArrayList<UsuarioDarwin>());
        if (this.getAvaliadores() != null) {
            for (UsuarioBeans avaliador : this.getAvaliadores()) {
                avaliadores.add((UsuarioDarwin) avaliador.toBusiness());
            }
        }
        etapa.setAvaliadores(avaliadores);
        
        List<Participante> participantes = Collections.synchronizedList(new ArrayList<Participante>());
        if (this.getParticipantes() != null) {
            for (ParticipanteBeans participante : this.getParticipantes()) {
            	participantes.add((Participante) participante.toBusiness());
            }
        }
        etapa.setParticipantes(participantes);

        List<Avaliacao> avaliacoes = Collections.synchronizedList(new ArrayList<Avaliacao>());
        if (this.getAvaliacoes() != null) {
            for (AvaliacaoBeans avaliacao : this.getAvaliacoes()) {
                avaliacoes.add((Avaliacao) avaliacao.toBusiness());
            }
        }
        etapa.setAvaliacoes(avaliacoes);

        List<Documentacao> documentacoes = Collections.synchronizedList(new ArrayList<Documentacao>());
        if (this.getDocumentacoes() != null) {
            for (DocumentacaoBeans documentacao : this.getDocumentacoes()) {
                documentacoes.add((Documentacao) documentacao.toBusiness());
            }
        }
        etapa.setDocumentacoes(documentacoes);
        etapa.setNotaMinima(this.getNotaMinima());
        etapa.setLimiteClassificados(this.getLimiteClassificados());
        etapa.setDivulgadoResultado(this.isDivulgadoResultado());
        return etapa;
    }

    @Override
    public Beans toBeans(Object object) {
        if (object != null) {
            if (object instanceof Etapa) {
                Etapa etapa = (Etapa) object;
                this.setCodEtapa(etapa.getCodEtapa());
                this.setTitulo(etapa.getTitulo());
                this.setDescricao(etapa.getDescricao());
                this.setDocumentacaoExigida(etapa.getDocumentacaoExigida());
                this.setEstado(etapa.getEstado());

                PeriodoBeans pb = null;
                if (etapa.getPeriodo() != null) {
                   pb = (PeriodoBeans) (new PeriodoBeans().toBeans(etapa.getPeriodo()));
                }
                this.setPeriodo(pb);

                this.setCriterioDeAvaliacao(etapa.getCriterioDeAvaliacao());
                
                if (etapa.getPrerequisito() != null) {
            		this.setPrerequisito((EtapaBeans) new EtapaBeans().toBeans(etapa.getPrerequisito()));
                }     

                List<UsuarioBeans> avaliadoresBeans = Collections.synchronizedList(new ArrayList<UsuarioBeans>());
                UsuarioBeans ubs;
                if (etapa.getAvaliadores() != null) {
                    for (UsuarioDarwin usuario : etapa.getAvaliadores()) {
                        ubs = (UsuarioBeans) (new UsuarioBeans().toBeans(usuario));
                        avaliadoresBeans.add(ubs);
                    }
                }
                this.setAvaliadores(avaliadoresBeans);
                
                List<ParticipanteBeans> participantes = Collections.synchronizedList(new ArrayList<ParticipanteBeans>());
                ParticipanteBeans p;
                if (etapa.getParticipantes() != null) {
                    for (Participante participante : etapa.getParticipantes()) {
                        p = (ParticipanteBeans) (new ParticipanteBeans().toBeans(participante));
                        participantes.add(p);
                    }
                }
                this.setParticipantes(participantes);

                List<AvaliacaoBeans> avaliacoesBeans = Collections.synchronizedList(new ArrayList<AvaliacaoBeans>());
                AvaliacaoBeans ab = null;
                if (etapa.getAvaliacoes() != null) {
                    for (Avaliacao avaliacao : etapa.getAvaliacoes()) {
                        ab = (AvaliacaoBeans) (new AvaliacaoBeans().toBeans(avaliacao));
                        avaliacoesBeans.add(ab);
                    }
                }
                this.setAvaliacoes(avaliacoesBeans);

                List<DocumentacaoBeans> documentacoes = Collections.synchronizedList(new ArrayList<DocumentacaoBeans>());
                DocumentacaoBeans db;
                if (etapa.getDocumentacoes() != null) {
                    for (Documentacao documentacao : etapa.getDocumentacoes()) {
                        db = (DocumentacaoBeans) (new DocumentacaoBeans().toBeans(documentacao));
                        documentacoes.add(db);
                    }
                }
                this.setDocumentacoes(documentacoes);
                this.setNotaMinima(etapa.getNotaMinima());
                this.setLimiteClassificados(etapa.getLimiteClassificados());
                this.setDivulgaResultado(etapa.isDivulgadoResultado());
                return this;
            } else {
                throw new IllegalArgumentException("O objeto a ser adicionado não é uma Etapa!");
            }
        } else {
            throw new NullPointerException("Etapa não pode ser nula!");
        }

    }
    
    
    @Override
	public boolean equals(final Object o) {
		return (this.getCodEtapa() == ((EtapaBeans) o).getCodEtapa());
	}

}
