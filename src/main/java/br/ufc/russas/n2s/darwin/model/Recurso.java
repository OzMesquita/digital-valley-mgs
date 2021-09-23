package br.ufc.russas.n2s.darwin.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recurso")
public class Recurso implements Serializable{
	//Esta classe foi criada pensando em futuras manutanções evolutivas
	private static final long serialVersionUID = -621919222767126918L;
	
	@Id
	@Column(name = "codRecurso")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long codRecurso;
	
	@Column(columnDefinition = "TEXT")
	private String descricao;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "periodo", referencedColumnName = "codPeriodo")
	private Periodo periodo;
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		if (periodo != null) {
			this.periodo = periodo;
		} else {
			throw new NullPointerException("Período não pode ser nulo!");
		}
	}
}
