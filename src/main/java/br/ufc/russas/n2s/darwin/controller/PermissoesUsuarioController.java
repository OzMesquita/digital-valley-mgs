/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufc.russas.n2s.darwin.beans.UsuarioBeans;
import br.ufc.russas.n2s.darwin.model.EnumPermissao;
import br.ufc.russas.n2s.darwin.model.Log;
import br.ufc.russas.n2s.darwin.model.UsuarioDarwin;
import br.ufc.russas.n2s.darwin.service.LogServiceIfc;
import br.ufc.russas.n2s.darwin.service.UsuarioServiceIfc;

/**
 *
 * @author Wallison Carlos, Gilberto
 */
@Controller("permissoesUsuarioController")
@RequestMapping("/permissoes")
public class PermissoesUsuarioController {
    
    private UsuarioServiceIfc usuarioServiceIfc;
    private LogServiceIfc logServiceIfc;
    
    @Autowired(required = true)
    public void setUsuarioServiceIfc(@Qualifier("usuarioServiceIfc") UsuarioServiceIfc usuarioServiceIfc) {
        this.usuarioServiceIfc = usuarioServiceIfc;
    }
    
    public LogServiceIfc getLogServiceIfc() {
    	return logServiceIfc;
    }
    @Autowired(required = true)
    public void setLogServiceIfc(@Qualifier("logServiceIfc") LogServiceIfc logServiceIfc) {
    	this.logServiceIfc = logServiceIfc;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String getIndex(Model model, HttpServletRequest request) {
    	UsuarioBeans usuario = (UsuarioBeans) request.getSession().getAttribute("usuarioDarwin");
    	if (usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
	        model.addAttribute("usuarios", this.usuarioServiceIfc.listaTodosUsuarios());
	        return "acessarPermissoes";
    	} else {return "error/404";}
    }
    
    @RequestMapping(value="/busca",method = RequestMethod.GET)
    public String getCampoDeBusca(Model model, @RequestParam("nomeUsuario") String nome) {
    	model.addAttribute("usuarios", this.usuarioServiceIfc.BuscaUsuariosPorNome(nome));
    	return "acessarPermissoes";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String getIndexSelectedUser(Model model, HttpServletRequest request) {
	        model.addAttribute("usuarios", this.usuarioServiceIfc.listaTodosUsuarios());
	        model.addAttribute("usuarioSelecionado", this.usuarioServiceIfc.getUsuario(Long.parseLong(request.getParameter("usuario")), 0));
	        return "acessarPermissoes"; 
    	
    }
    
    @RequestMapping(value = "/atualizar", method = RequestMethod.POST)
    public String adiciona(@RequestParam("codUsuario") long codUsuario, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        try {

            UsuarioBeans autenticado = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            usuarioServiceIfc.setUsuario(autenticado);
            UsuarioBeans usuario = usuarioServiceIfc.getUsuario(codUsuario, 0);
            String[] permissoes  = request.getParameterValues("codPermissao");
            List<EnumPermissao> permisoesAtualizadas = new ArrayList<>();
            if (permissoes != null) {
            	 EnumPermissao p = null;
            	 for (String num : permissoes) {
                    int permissao = Integer.parseInt(num);
                    p = EnumPermissao.getValor(permissao);
                    permisoesAtualizadas.add(p);
                }
            }
            usuarioServiceIfc.atualizaNiveis(usuario, permisoesAtualizadas);
            UsuarioDarwin us = (UsuarioDarwin)((UsuarioBeans) session.getAttribute("usuarioDarwin")).toBusiness();
            this.getLogServiceIfc().adicionaLog(new Log(LocalDate.now(), us, null, "O(A) usuario(a) "+ us.getNome()+" portador do CPF: "+us.getCPF()+", modificou a permissão do usuário detentor do CPF: "+usuario.getCPF()+" em "+LocalDate.now()+"."));
            session.setAttribute("mensagem", "Permissões do usuário '<b>"+usuario.getNome()+"</b>' atualizadas com sucesso!");
            session.setAttribute("status", "success");
            model.addAttribute("usuarios", this.usuarioServiceIfc.listaTodosUsuarios());
            model.addAttribute("usuarioSelecionado", this.usuarioServiceIfc.getUsuario(Long.parseLong(request.getParameter("codUsuario")), 0));
            return "redirect:/permissoes";
        } catch (NumberFormatException e) {
            session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
            return "redirect:/permissoes";
        } catch (IllegalArgumentException | IllegalAccessException e) {
            session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
            return "redirect:/permissoes";
        } catch (Exception e) {
        	session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
            return "redirect:/permissoes";
		}
    }
}
