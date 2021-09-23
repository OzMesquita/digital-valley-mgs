/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Wallison Carlos
 */
public class UsuarioDarwinProxy extends UsuarioDarwin implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 950458079620457641L;
	
	private UsuarioDarwin usuario;
    
    public UsuarioDarwinProxy(UsuarioDarwin usuario) {
        setUsuario(usuario);
    }

    public UsuarioDarwin getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDarwin usuario) {
        if (usuario != null) {
            this.usuario = usuario;
        } else {
            throw new NullPointerException("Usuário do Darwin não pode ser vazio!");
        }
    }
    
    @Override
    public UsuarioDarwin adicionaNivel(UsuarioDarwin usuario, EnumPermissao permissao) throws IllegalAccessException{
        if (getUsuario().getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
            return super.adicionaNivel(usuario, permissao);
        } else {
            throw new IllegalAccessException("Você não é um administrador do Darwin!");
        }
    }
    
    @Override
    public UsuarioDarwin atualizaNiveis(UsuarioDarwin usuario, List<EnumPermissao> permissoes) throws IllegalAccessException{
        if (getUsuario().getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
        	permissoes.add(EnumPermissao.PARTICIPANTE);
            return super.atualizaNiveis(usuario, permissoes);
        } else {
            throw new IllegalAccessException("Você não é um administrador do Darwin!");
        }
    }
    
    
    @Override
    public UsuarioDarwin removeNivel(UsuarioDarwin usuario, EnumPermissao permissao) throws IllegalAccessException{
        if (getUsuario().getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
            return super.removeNivel(usuario, permissao);
        } else {
            throw new IllegalAccessException("Você não é um administrador do Darwin!");
        }
    }
}
