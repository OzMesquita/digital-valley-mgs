package br.ufc.russas.n2s.darwin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoParticipanteSelecao {
	
	private int colocacao;
	private Participante participante;
	private List<Etapa> etapas = Collections.synchronizedList(new ArrayList<Etapa>());
	private List<Float> notasEtapas = Collections.synchronizedList(new ArrayList<Float>());
	private float mediaGeral;
	private boolean aprovado;

	public int getColocacao() {
		return colocacao;
	}
	
	public void setColocacao(int colocacao) {
		this.colocacao = colocacao;
	}
	
	public Participante getParticipante() {
		return participante;
	}
	
	public void setParticipante(Participante participante) {
		this.participante = participante;
	}
	
	public List<Etapa> getEtapas() {
		return etapas;
	}
	
	public void setEtapas(List<Etapa> etapas) {
		this.etapas = etapas;
	}
	
	public List<Float> getNotasEtapas() {
		return notasEtapas;
	}
	
	public void setNotasEtapas(List<Float> notasEtapas) {
		this.notasEtapas = notasEtapas;
	}
	
	
	public float getMediaGeral() {
		return mediaGeral;
	}
	
	public void setMediaGeral(float mediaGeral) {
		this.mediaGeral = mediaGeral;
	}
	
	public boolean isAprovado() {
		return aprovado;
	}
	
	public void setAprovado(boolean aprovado) {
		this.aprovado = aprovado;
	}
}
