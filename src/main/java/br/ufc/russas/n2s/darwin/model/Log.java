package br.ufc.russas.n2s.darwin.model;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Converter(autoApply = true)
@Table(name = "log")
public class Log implements AttributeConverter<LocalDate, Date>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long codLog;
	
	private LocalDate data;
	@ManyToOne
    @JoinColumn(name="usuario", referencedColumnName="codUsuario")
	private UsuarioDarwin usuario;
	@ManyToOne
    @JoinColumn(name="selecao", referencedColumnName="codSelecao")
	private Selecao selecao;
	@Column(columnDefinition = "TEXT")
    private String modificacao;
	
    public Log(LocalDate data, UsuarioDarwin usuario, Selecao selecao, String modificacao) throws IllegalArgumentException {
    	this.setData(data);
    	this.setUsuario(usuario);
    	this.setSelecao(selecao);
    	this.setModificacao(modificacao);
    }
    
    
	public long getCodLog() {
		return this.codLog;
	}
	public void setCodLog(long codLog) {
		if (codLog > 0) {
			this.codLog = codLog;
		}
	}
	
	public LocalDate getData() {
        return this.data;
    }

    public void setData(LocalDate data) {
        if (data != null) {
        	this.data = data;
        } else {
        	throw new IllegalArgumentException("Data não pode ser nula!");
        }
    }
	
	public UsuarioDarwin getUsuario() {
		return this.usuario;
	}
	
	public void setUsuario(UsuarioDarwin usuario) {
		if (usuario != null) {
			this.usuario = usuario;
		} else {
			throw new IllegalArgumentException("Usuário não pode ser nulo!");
		}
	}
	public Selecao getSelecao() {
		return this.selecao;
	}
	
	public void setSelecao(Selecao selecao) {
		this.selecao = selecao;
	}
		
	@Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return (attribute == null ? null : Date.valueOf(attribute));
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return (dbData == null ? null : dbData.toLocalDate());
    }
    
    public String getModificacao() {
		return modificacao;
	}
	public void setModificacao(String modificacao) {
		if (modificacao != null) {
			this.modificacao = modificacao;
		} else {
			throw new IllegalArgumentException("descrição da modificação não pode ser nula!");
		}
	}
    
}
