/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.model;

import br.ufc.russas.n2s.darwin.beans.UsuarioBeans;
import br.ufc.russas.n2s.darwin.dao.UsuarioDAOIfc;
import br.ufc.russas.n2s.darwin.dao.UsuarioDAOImpl;
import br.ufc.russas.n2s.darwin.model.exception.IllegalCodeException;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Gilberto, Wallison
 */
@Entity
@Table(name="usuario")
public class UsuarioDarwin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codUsuario")
    private long codUsuario;
    @Column(name = "codUsuarioControleDeAcesso")
    private long codUsuarioControleDeAcesso;
    private String nome;
    private String email;
    @Column
    @Enumerated
    @ElementCollection(targetClass = EnumPermissao.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<EnumPermissao> permissoes;
    private boolean recebeEmail;

    public long getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(long codUsuario) {
        if(codUsuario>0)
            this.codUsuario = codUsuario;

            //throw new IllegalCodeException("Código de usuário deve ser maior de zero!");
    }

    public List<EnumPermissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<EnumPermissao> permissoes) {
        this.permissoes = permissoes;
    }

    public long getCodUsuarioControleDeAcesso() {
        return codUsuarioControleDeAcesso;
    }

    public void setCodUsuarioControleDeAcesso(long codUsuarioControleDeAcesso) {
        this.codUsuarioControleDeAcesso = codUsuarioControleDeAcesso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
    	return this.email;
    }
    
    public void setEmail(String email) {
    	if (email != null) {
    		this.email = email;
    	} else {
    		throw new NullPointerException("E-mail não pode ser vazio!");
    	}
    }
    
    public boolean isRecebeEmail() {
    	return this.recebeEmail;
    }
    
    public void setRecebeEmail(boolean recebeEmail) {
    	this.recebeEmail = recebeEmail;
    }
    
    public UsuarioDarwin adicionaNivel(UsuarioDarwin usuario, EnumPermissao permissao) throws IllegalAccessException{
        if (!usuario.getPermissoes().contains(permissao)) {
            usuario.getPermissoes().add(permissao);
            return usuario;
        } else {
            throw new IllegalArgumentException("O Usuário "+usuario.getNome()+" já possui a permissão de ".concat(permissao.toString()));
        }
    }
    
    public UsuarioDarwin atualizaNiveis(UsuarioDarwin usuario, List<EnumPermissao> permissoes) throws IllegalAccessException{
        if (usuario!= null && permissoes!= null) {
            usuario.setPermissoes(permissoes);
            return usuario;
        } else {
            throw new IllegalArgumentException("Erro ao tentar atualizar as permissões do usuário");
        }
    }
    
   
    public UsuarioDarwin removeNivel(UsuarioDarwin usuario, EnumPermissao permissao) throws IllegalAccessException{
        if (usuario.getPermissoes().contains(permissao)) {
            usuario.getPermissoes().remove(permissao);
            return usuario;
        } else {
            throw new IllegalArgumentException("O Usuário "+usuario.getNome()+" não possui a permissão de ".concat(permissao.toString()));
        }
    }
    
    
    @Override
    public boolean equals(Object o){
        return (this.getCodUsuario() == ((UsuarioDarwin) o).getCodUsuario());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (int) (this.codUsuario ^ (this.codUsuario >>> 32));
        return hash;
    }
}
