/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.service;

import br.ufc.russas.n2s.darwin.beans.UsuarioBeans;
import br.ufc.russas.n2s.darwin.dao.UsuarioDAOIfc;
import br.ufc.russas.n2s.darwin.model.EnumPermissao;
import br.ufc.russas.n2s.darwin.model.UsuarioDarwin;
import br.ufc.russas.n2s.darwin.model.UsuarioDarwinProxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Wallison Carlos
 */
public class UsuarioServiceImpl implements UsuarioServiceIfc {
    
    private UsuarioDAOIfc usuarioDAOIfc;
    private UsuarioBeans usuario;
    
    public UsuarioDAOIfc getUsuarioDAOIfc() {
        return usuarioDAOIfc;
    }

    @Autowired(required = true)
    public void setUsuarioDAOIfc(@Qualifier("usuarioDAOIfc")UsuarioDAOIfc usuarioDAOIfc) {
        this.usuarioDAOIfc = usuarioDAOIfc;
    }
    
    @Override
    public void setUsuario(UsuarioBeans usuario) {
        this.usuario = usuario;
    }
    
    
    @Override
    public UsuarioBeans adicionaUsuario(UsuarioBeans usuario) {
        return (UsuarioBeans) new UsuarioBeans().toBeans(this.getUsuarioDAOIfc().adicionaUsuario((UsuarioDarwin)usuario.toBusiness()));
    }

    @Override
    public UsuarioBeans atualizaUsuario(UsuarioBeans usuario) {
        return (UsuarioBeans) new UsuarioBeans().toBeans(this.getUsuarioDAOIfc().atualizaUsuario((UsuarioDarwin) usuario.toBusiness()));
    }

    @Override
    public void removeUsuario(UsuarioBeans usuario) {
        this.getUsuarioDAOIfc().removeUsuario((UsuarioDarwin) usuario.toBusiness());
    }

    @Override
    public List<UsuarioBeans> listaTodosUsuarios() {
        List<UsuarioDarwin> result = this.getUsuarioDAOIfc().ListaEmOdermAlfabetica();
        List<UsuarioBeans> usuarios = Collections.synchronizedList(new ArrayList<UsuarioBeans>());
        for(UsuarioDarwin usuario : result){
            usuarios.add((UsuarioBeans) new UsuarioBeans().toBeans(usuario));
        }
        return usuarios;
    }
    
    @Override
    public List<UsuarioBeans> BuscaUsuariosPorNome(String nome) {
        List<UsuarioDarwin> result = this.getUsuarioDAOIfc().BuscaUsuariosPorNome(nome);
        List<UsuarioBeans> usuarios = Collections.synchronizedList(new ArrayList<UsuarioBeans>());
        for(UsuarioDarwin usuario : result){
            usuarios.add((UsuarioBeans) new UsuarioBeans().toBeans(usuario));
        }
        return usuarios;
    }

    @Override
    public UsuarioBeans getUsuario(long codUsuario, long codUsuarioControleDeAcesso) {
        UsuarioDarwin usuario = new UsuarioDarwin();
        usuario.setCodUsuario(codUsuario);
        UsuarioDarwin u = this.getUsuarioDAOIfc().getUsuario(usuario);
        if(u != null){
            return (UsuarioBeans) new UsuarioBeans().toBeans(u);
        }else{
            return null;
        }
    }
    
    @Override
    public UsuarioBeans getUsuarioControleDeAcesso(long codUsuarioControleDeAcesso) {
        UsuarioDarwin usuario = new UsuarioDarwin();
        usuario.setCodUsuarioControleDeAcesso(codUsuarioControleDeAcesso);
        UsuarioDarwin u = this.getUsuarioDAOIfc().getUsuarioControleDeAcesso(usuario);
        if(u != null){
            return (UsuarioBeans) new UsuarioBeans().toBeans(u);
        }else{
            return null;
        }
    }

    @Override
    public void adicionaNivel(UsuarioBeans usuario, EnumPermissao permissao) throws IllegalAccessException {
        UsuarioDarwin u = (UsuarioDarwin) this.usuario.toBusiness();
        UsuarioDarwinProxy up = new UsuarioDarwinProxy(u);
        usuario  = (UsuarioBeans) usuario.toBeans(up.adicionaNivel((UsuarioDarwin)usuario.toBusiness(), permissao));
        atualizaUsuario(usuario);
    }
    
    @Override
    public void atualizaNiveis(UsuarioBeans usuario, List<EnumPermissao> permissoes) throws IllegalAccessException {
        UsuarioDarwin u = (UsuarioDarwin) this.usuario.toBusiness();
        UsuarioDarwinProxy up = new UsuarioDarwinProxy(u);
        usuario  = (UsuarioBeans) usuario.toBeans(up.atualizaNiveis((UsuarioDarwin)usuario.toBusiness(), permissoes));
        atualizaUsuario(usuario);
    }

    @Override
    public void removeNivel(UsuarioBeans usuario, EnumPermissao permissao) throws IllegalAccessException {
        UsuarioDarwin u = (UsuarioDarwin) this.usuario.toBusiness();
        UsuarioDarwinProxy up = new UsuarioDarwinProxy(u);
        usuario  = (UsuarioBeans) usuario.toBeans(up.removeNivel((UsuarioDarwin)usuario.toBusiness(), permissao));
        atualizaUsuario(usuario);
    }
    
    @Override
    public List<UsuarioBeans> listaUsuariosComPermissao(EnumPermissao permissao) {
        List<UsuarioBeans> lista = Collections.synchronizedList(new ArrayList<UsuarioBeans>());
        List<UsuarioBeans> resultado = this.listaTodosUsuarios();
        for (UsuarioBeans ub : resultado) {
            if (ub.getPermissoes().contains(permissao)) {
                lista.add(ub);
            }
        }
        return lista;
    }
    
    
}
