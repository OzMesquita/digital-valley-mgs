/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.controller;

import br.ufc.russas.n2s.darwin.beans.EtapaBeans;
import br.ufc.russas.n2s.darwin.beans.ParticipanteBeans;
import br.ufc.russas.n2s.darwin.beans.PeriodoBeans;
import br.ufc.russas.n2s.darwin.beans.SelecaoBeans;
import br.ufc.russas.n2s.darwin.beans.UsuarioBeans;
import br.ufc.russas.n2s.darwin.model.Email;
import br.ufc.russas.n2s.darwin.model.EnumCriterioDeAvaliacao;
import br.ufc.russas.n2s.darwin.model.EnumEstadoEtapa;
import br.ufc.russas.n2s.darwin.model.EnumEstadoSelecao;
import br.ufc.russas.n2s.darwin.model.EnumPermissao;
import br.ufc.russas.n2s.darwin.model.Log;
import br.ufc.russas.n2s.darwin.model.Periodo;
import br.ufc.russas.n2s.darwin.model.Recurso;
import br.ufc.russas.n2s.darwin.model.Selecao;
import br.ufc.russas.n2s.darwin.model.UsuarioDarwin;
import br.ufc.russas.n2s.darwin.model.exception.IllegalCodeException;
import br.ufc.russas.n2s.darwin.service.EtapaServiceIfc;
import br.ufc.russas.n2s.darwin.service.LogServiceIfc;
import br.ufc.russas.n2s.darwin.service.SelecaoServiceIfc;
import br.ufc.russas.n2s.darwin.service.UsuarioServiceIfc;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

/**
 *
 * @author Gilberto
 */
@Controller("editarEtapaController")
@RequestMapping("/editarEtapa")
public class EditarEtapaController {

    private EtapaServiceIfc etapaServiceIfc;
    private SelecaoServiceIfc selecaoServiceIfc;
    private UsuarioServiceIfc usuarioServiceIfc;
    private LogServiceIfc logServiceIfc;
    
    public EtapaServiceIfc getEtapaServiceIfc() {
        return etapaServiceIfc;
    }

    @Autowired(required = true)
    public void setEtapaServiceIfc(@Qualifier("etapaServiceIfc")EtapaServiceIfc etapaServiceIfc) {
        this.etapaServiceIfc = etapaServiceIfc;
    }

    public SelecaoServiceIfc getSelecaoServiceIfc() {
        return selecaoServiceIfc;
    }
    @Autowired(required = true)
    public void setSelecaoServiceIfc(@Qualifier("selecaoServiceIfc") SelecaoServiceIfc selecaoServiceIfc) {
        this.selecaoServiceIfc = selecaoServiceIfc;
    }

    public UsuarioServiceIfc getUsuarioServiceIfc() {
        return usuarioServiceIfc;
    }
    @Autowired(required = true) 
    public void setUsuarioServiceIfc(@Qualifier("usuarioServiceIfc")UsuarioServiceIfc usuarioServiceIfc) {
        this.usuarioServiceIfc = usuarioServiceIfc;
    }
    public LogServiceIfc getLogServiceIfc() {
    	return logServiceIfc;
    }
    @Autowired
    public void setLogServiceIfc(@Qualifier("logServiceIfc")LogServiceIfc logServiceIfc) {
    	this.logServiceIfc = logServiceIfc;
    }       
    
    @RequestMapping(value="/{codSelecao}/{codEtapa}", method = RequestMethod.GET)
    public String getIndex(@PathVariable long codSelecao, @PathVariable long codEtapa, Model model, HttpServletRequest request) {
        EtapaBeans etapaBeans = this.etapaServiceIfc.getEtapa(codEtapa);
        SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
        UsuarioBeans usuario = (UsuarioBeans) request.getSession().getAttribute("usuarioDarwin");
        if (selecao.getResponsaveis().contains(usuario) || usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
	        List<UsuarioBeans> usuarios = this.getUsuarioServiceIfc().listaUsuariosComPermissao(EnumPermissao.AVALIADOR);
	        if (etapaBeans.getCodEtapa() == selecao.getInscricao().getCodEtapa()) {
	            model.addAttribute("tipo", "inscricao"); 
	        } else {
	            model.addAttribute("tipo", "etapa");
	        }
	        
	        List<Long> codAvaliadores = Collections.synchronizedList(new ArrayList<>());
	        List<String> nomeAvaliadores = Collections.synchronizedList(new ArrayList<>());
	        
	        for (UsuarioBeans avaliador : etapaBeans.getAvaliadores()) {
	        	codAvaliadores.add(avaliador.getCodUsuario());
	        	nomeAvaliadores.add(avaliador.getNome());
	        }
	        String json = new Gson().toJson(nomeAvaliadores);
	        model.addAttribute("selecao", selecao);
	        model.addAttribute("etapa", etapaBeans);
	        model.addAttribute("codAvaliadores", codAvaliadores);
	        model.addAttribute("nomeAvaliadores", json);
	        model.addAttribute("listaDocumentos", new Gson().toJson(etapaBeans.getDocumentacaoExigida()));
	        model.addAttribute("listaNumeroDocumentos", etapaBeans.getDocumentacaoExigida().size());
	        model.addAttribute("listaDocumentosOp", new Gson().toJson(etapaBeans.getDocumentacaoOpcional()));
	        model.addAttribute("listaNumeroDocumentosOp", etapaBeans.getDocumentacaoOpcional().size());
	        model.addAttribute("usuarios", usuarios);
	        return "editar-etapa";
        } else { return "error/404";}
    }

    @RequestMapping(value="/{codSelecao}/{codEtapa}", method = RequestMethod.POST)
    public String atualiza(@PathVariable long codSelecao, @PathVariable long codEtapa, EtapaBeans etapa, BindingResult result, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	try{
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = this.selecaoServiceIfc.getSelecao(codSelecao);
            EtapaBeans etapaBeans= this.etapaServiceIfc.getEtapa(codEtapa);
            if (etapaBeans.getEstado().equals(EnumEstadoEtapa.ANDAMENTO) && selecao.isDivulgada()) {throw new Exception("Etapa em andamento no pode ser editada.");}
            
            if (usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR) || selecao.getResponsaveis().contains(usuario)) {
            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            	if (LocalDate.parse(request.getParameter("dataInicio"), formatter).isBefore(LocalDate.now()) && selecao.isDivulgada() && !etapaBeans.getEstado().equals(EnumEstadoEtapa.ESPERA)) {
            		throw new Exception("Após iniciada a etapa, apenas a data de término pode ser prorrogada.");
            	}
            	etapaBeans.setPeriodo(new PeriodoBeans(0, LocalDate.parse(request.getParameter("dataInicio"), formatter), LocalDate.parse(request.getParameter("dataTermino"), formatter)));
	           
            	 if (request.getParameter("dataInicioRecurso")!= null && (request.getParameter("dataInicioRecurso").length() >= 8 ) && request.getParameter("dataTerminoRecurso")!= null && (request.getParameter("dataTerminoRecurso").length() >= 8)) {
                 	Recurso recurso = new Recurso();
                 	DateTimeFormatter formatte = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                 	PeriodoBeans pb =new PeriodoBeans(0,LocalDate.parse(request.getParameter("dataInicioRecurso"), formatte), LocalDate.parse(request.getParameter("dataTerminoRecurso"), formatte));
                 	recurso.setPeriodo((Periodo) pb.toBusiness());
                 	etapaBeans.setRecurso(recurso);
                 } else { 
                 	etapaBeans.setRecurso(null);
                 }
            	 
            	if (etapaBeans.getPeriodo().getInicio().isAfter(LocalDate.now()) || !selecao.isDivulgada()) {
		            String[] codAvaliadores = request.getParameterValues("codAvaliadores");
		            String[] documentosExigidos = request.getParameterValues("documentosExigidos");
		            String[] documentosOpcionais = request.getParameterValues("documentosOpcionais");
		            int criterio = Integer.parseInt(request.getParameter("criterio"));
		            if (criterio == 1) {
		                etapaBeans.setCriterioDeAvaliacao(EnumCriterioDeAvaliacao.NOTA);
		                etapaBeans.setNotaMinima(etapa.getNotaMinima());
		            } else if(criterio == 2) {
		                etapaBeans.setCriterioDeAvaliacao(EnumCriterioDeAvaliacao.APROVACAO);
		            } else if(criterio == 3) {
		                etapaBeans.setCriterioDeAvaliacao(EnumCriterioDeAvaliacao.DEFERIMENTO);
		            }
		            
		            etapaBeans.setTitulo(etapa.getTitulo());
		            etapaBeans.setDescricao(etapa.getDescricao());
		            long codPrerequisito = Long.parseLong(request.getParameter("prereq"));
		            EtapaBeans pre = this.etapaServiceIfc.getEtapa(codPrerequisito);
		            etapaBeans.setPrerequisito(pre);
		           
		            ArrayList<UsuarioBeans> avaliadores = new ArrayList<>();
		            if (codAvaliadores != null) {
		                for (String cod : codAvaliadores) {
		                    UsuarioBeans u = this.getUsuarioServiceIfc().getUsuario(Long.parseLong(cod),0);
		                    if (u != null) {
		                        avaliadores.add(u);
		                    }
		                }
		            }
		            
		            if (documentosExigidos != null) {
		            	ArrayList<String> docs = new ArrayList<>();
		                for(String documento : documentosExigidos) {
		                    docs.add(documento);
		                }
		                etapaBeans.setDocumentacaoExigida(docs);
		            } else {
		            	etapaBeans.setDocumentacaoExigida(new ArrayList<>());
		            }
		            if (documentosOpcionais != null) {
		            	ArrayList<String> docsOp = new ArrayList<>();
		                for(String documento : documentosOpcionais){
		                    docsOp.add(documento);
		                }
		                etapaBeans.setDocumentacaoOpcional(docsOp);
		            } else {
		            	etapaBeans.setDocumentacaoExigida(new ArrayList<>());
		            }
		            etapaBeans.setAvaliadores(avaliadores);
		          /*  this.getSelecaoServiceIfc().setUsuario(usuario);
		            
		            int index=0;
		            List<EtapaBeans> etapas = selecao.getEtapas();
		            for(EtapaBeans etapaIterator : etapas) {
		            	if(etapaIterator.getCodEtapa()==etapaBeans.getCodEtapa()) {
		            		selecao.getEtapas().remove(etapaIterator);
		            		selecao.getEtapas().add(index, etapaBeans);
		            		break;
		            	}
		            	index++;
		            }
		            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
		            */
		            this.getEtapaServiceIfc().atualizaEtapa(etapaBeans);
		            selecao = this.getSelecaoServiceIfc().getSelecao(selecao.getCodSelecao());
		            this.getLogServiceIfc().adicionaLog(new Log(LocalDate.now(),(UsuarioDarwin)usuario.toBusiness(), (Selecao) selecao.toBusiness(), "O(A) usuario(a) "+ usuario.getNome()+" modificou a etapa "+etapa.getTitulo()+" na seleção "+selecao.getTitulo()+" em "+LocalDate.now()+"."));
		            session.setAttribute("status", "success");
		            session.setAttribute("mensagem", "Etapa atualizada com sucesso!");
		            session.setAttribute("selecao", selecao);
		            return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
	            }
	            List <EtapaBeans> subsequentes = selecao.getEtapas();
	            Periodo novoP = (Periodo) etapaBeans.getPeriodo().toBusiness();
	            for(EtapaBeans sub: subsequentes){
	            	if(sub.getCodEtapa()!=codEtapa) {
	            		Periodo periodo =(Periodo) sub.getPeriodo().toBusiness();
	            		if(periodo.isColide(novoP)) {
	            			throw new IllegalCodeException("Periodo Inválido!");
	            		}
            		}
	            }
	            
	            
            } else {
            	session.setAttribute("selecao", selecao); 
            	session.setAttribute("mensagem", "Etapa não pode ser atualizada! Pois ela já foi iniciada!");
                session.setAttribute("status", "warning");
        		return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
            }
            this.getSelecaoServiceIfc().setUsuario(usuario);
            int index=0;
            List<EtapaBeans> etapas = selecao.getEtapas();
            for(EtapaBeans etapaIterator : etapas) {
            	if(etapaIterator.getCodEtapa()==etapaBeans.getCodEtapa()) {
            		selecao.getEtapas().remove(etapaIterator);
            		selecao.getEtapas().add(index, etapaBeans);
            		break;
            	}
            	index++;
            }
           
            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
            this.getLogServiceIfc().adicionaLog(new Log(LocalDate.now(),(UsuarioDarwin)usuario.toBusiness(), (Selecao) selecao.toBusiness(), "O(A) usuario(a) "+ usuario.getNome()+" modificou a etapa "+etapa.getTitulo()+" na seleção "+selecao.getTitulo()+" em "+LocalDate.now()+"."));
            session.setAttribute("status", "success");
            session.setAttribute("mensagem", "Etapa atualizada com sucesso!");
            session.setAttribute("selecao", selecao);
            return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
        } catch (IllegalAccessException e) {
        	e.printStackTrace();
        	model.addAttribute("mensagem", e.getMessage());
        	model.addAttribute("status", "danger");
            return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
        }catch (IllegalCodeException e) {
        	session.setAttribute("mensagem", "Etapa não pode ser atualizada! Verifique o conflito entre periodos com outras etapas!");
            session.setAttribute("status", "warning");
    		return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
    	} catch (IllegalArgumentException e) {
        	session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "warning");
    		return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
    	} catch (Exception e) {
    		e.printStackTrace();
        	session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "warning");
    		return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
    	}
         
    }
    
    @RequestMapping(value="/{codSelecao}/inscricao/{codInscricao}", method = RequestMethod.POST)
    public String atualizaInscricao(@PathVariable long codSelecao, @PathVariable long codInscricao, EtapaBeans inscricao, BindingResult result, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	try{
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = this.getSelecaoServiceIfc().getSelecao(codSelecao);
            
            EtapaBeans inscricaoBeans = this.getEtapaServiceIfc().getEtapa(codInscricao);
            if (usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR) || selecao.getResponsaveis().contains(usuario)) {
            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            	
            	if(selecao.getEstado() != EnumEstadoSelecao.EMEDICAO){
            		throw new Exception("A etapa " + inscricaoBeans.getTitulo() + " não pode ser editada pois a seleção não está em edição.");
            	}
            	if (LocalDate.parse(request.getParameter("dataInicio"), formatter).isBefore(LocalDate.now()) && selecao.isDivulgada()) {
            		throw new Exception("Após iniciada a etapa, apenas a data de término pode ser prorrogada.");
            	}
            	
            	if(selecao.getEtapas().size() > 0 && selecao.getEtapas().get(0)!=null && inscricaoBeans.getPeriodo().getTermino().isBefore(selecao.getEtapas().get(0).getPeriodo().getInicio())){
            		session.setAttribute("selecao", selecao);
    	            session.setAttribute("mensagem", "Etapa "+inscricaoBeans.getTitulo()+" não pode ter data de início igual ou após a data da "+selecao.getEtapas().get(0).getTitulo());
    	            session.setAttribute("status", "warning");
    	            return "redirect:/selecao/" + selecao.getCodSelecao();
            	} else {
            		inscricaoBeans.setPeriodo(new PeriodoBeans(0, LocalDate.parse(request.getParameter("dataInicio"), formatter), LocalDate.parse(request.getParameter("dataTermino"), formatter)));
    	            if (request.getParameter("dataInicioRecurso")!= null && (request.getParameter("dataInicioRecurso").length() >= 8 ) && request.getParameter("dataTerminoRecurso")!= null && (request.getParameter("dataTerminoRecurso").length() >= 8)) {
    	            	Recurso recurso = new Recurso();
    	            	DateTimeFormatter formatte = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	            	PeriodoBeans pb =new PeriodoBeans(0,LocalDate.parse(request.getParameter("dataInicioRecurso"), formatte), LocalDate.parse(request.getParameter("dataTerminoRecurso"), formatte));
    	            	recurso.setPeriodo((Periodo) pb.toBusiness());
    	            	inscricaoBeans.setRecurso(recurso);
    	            } else { 
    	            	inscricaoBeans.setRecurso(null);
    	            }
    	            
    	            if (inscricaoBeans.getPeriodo().getInicio().isAfter(LocalDate.now()) || !selecao.isDivulgada() || usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
    		            String[] codAvaliadores = request.getParameterValues("codAvaliadores");
    		            String[] documentosExigidos = request.getParameterValues("documentosExigidos");
    		            String[] documentosOpcionais = request.getParameterValues("documentosOpcionais");
    		            inscricaoBeans.setTitulo(inscricao.getTitulo());
    		            inscricaoBeans.setDescricao(inscricao.getDescricao());
    		            
    		            ArrayList<UsuarioBeans> avaliadores = new ArrayList<>();
    		            try {
    			            if (codAvaliadores != null) {
    			                for (String cod : codAvaliadores) {
    			                	if (cod.contains("-")) {
    			                		cod = cod.substring(0,cod.indexOf("-"));
    			                	}
    			                    UsuarioBeans u = this.getUsuarioServiceIfc().getUsuario(Long.parseLong(cod),0);
    			                    if (u != null) {
    			                        avaliadores.add(u);
    			                    }
    			                }
    			            }
    		            } catch (NumberFormatException e) {
    		            	session.setAttribute("mensagem", "Ocorreu um erro ao cadastrar avaliador(es)!");
    		                session.setAttribute("status", "danger");
    		            	return "redirect:/editarEtapa/" + codSelecao+"/"+codInscricao;
    		            }
    		            if (documentosExigidos != null) {
    		                ArrayList<String> docs = new ArrayList<>();
    		                for (String documento : documentosExigidos) {
    		                    docs.add(documento);
    		                }
    		                inscricaoBeans.setDocumentacaoExigida(docs);
    		            } else {
    		            	inscricaoBeans.setDocumentacaoExigida(new ArrayList<>());
    		            }
    		            
    		            if (documentosOpcionais != null) {
    		            	ArrayList<String> docsOp = new ArrayList<>();
    		                for(String documento : documentosOpcionais){
    		                    docsOp.add(documento);
    		                }
    		                inscricaoBeans.setDocumentacaoOpcional(docsOp);
    		            } else {
    		            	inscricaoBeans.setDocumentacaoOpcional(new ArrayList<>());
    		            }
    		            inscricaoBeans.setAvaliadores(avaliadores);
    		            this.getSelecaoServiceIfc().setUsuario(usuario);
    		            selecao.setInscricao(inscricaoBeans);
    		            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
    		            this.getLogServiceIfc().adicionaLog(new Log(LocalDate.now(),(UsuarioDarwin)usuario.toBusiness(), (Selecao) selecao.toBusiness(), "O(A) usuario(a) "+ usuario.getNome()+" modificou a etapa "+inscricaoBeans.getTitulo()+" na seleção "+selecao.getTitulo()+" em "+LocalDate.now()+"."));
    		            session.setAttribute("selecao", selecao);
    		            session.setAttribute("mensagem", "Etapa "+inscricaoBeans.getTitulo()+" atualizada com sucesso!");
    		            session.setAttribute("status", "success");
    		            return "redirect:/selecao/" + selecao.getCodSelecao();
    	            }
    	            List <EtapaBeans> subsequentes = selecao.getEtapas();
    	            Periodo novoP = (Periodo) inscricaoBeans.getPeriodo().toBusiness();
    	            for (EtapaBeans sub: subsequentes) {
    	            	if (sub.getCodEtapa()!=inscricao.getCodEtapa()) {
    	            		Periodo periodo =(Periodo) sub.getPeriodo().toBusiness();
    	            		if (periodo.isColide(novoP)) {
    	            			throw new IllegalArgumentException("Periodo Inválido!");
    	            		}
                		}
    	            }
    	            
    	            this.getSelecaoServiceIfc().setUsuario(usuario);
    	            selecao.setInscricao(inscricaoBeans);
    	            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
    	            session.setAttribute("selecao", selecao);
    	            session.setAttribute("mensagem", "Etapa "+inscricaoBeans.getTitulo()+" atualizada com sucesso!");
    	            session.setAttribute("status", "success");
    	            return "redirect:/selecao/" + selecao.getCodSelecao();
            	}
	            
	        } else {
            	session.setAttribute("selecao", selecao);
            	session.setAttribute("mensagem", "Etapa já foi iniciada, não é mais possível realizar edições!");
	            session.setAttribute("status", "warning");
	            return "redirect:/editarEtapa/" + selecao.getCodSelecao()+"/"+codInscricao;
            }
        }catch (IllegalArgumentException e) {
        	session.setAttribute("mensagem", "Etapa não pode ser atualizada! Verifique o conflito entre periodos com outras etapas!");
            session.setAttribute("status", "warning");
    		return "redirect:/editarEtapa/" +codSelecao+"/"+codInscricao;
    	}catch (Exception e) {
    		e.printStackTrace();
    		session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
    		return "redirect:/editarEtapa/" +codSelecao+"/"+codInscricao;
    	}
         
    }
    
    @RequestMapping(value="/divulgarResultado/{codSelecao}/{codEtapa}", method = RequestMethod.GET)
    public String divulgaResultado(@PathVariable long codSelecao, @PathVariable long codEtapa, BindingResult result, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	try{
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
            if (selecao.getResponsaveis().contains(usuario) || usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
	            EtapaBeans etapa = etapaServiceIfc.getEtapa(codEtapa);
	            etapa.setDivulgaResultado(true);
	            etapaServiceIfc.atualizaEtapa(etapa);
	            this.getLogServiceIfc().adicionaLog(new Log(LocalDate.now(),(UsuarioDarwin)usuario.toBusiness(), (Selecao) selecao.toBusiness(), "O(A) usuario(a) "+ usuario.getNome()+" divulgou o resultado da etapa "+etapa.getTitulo()+" na seleção "+selecao.getTitulo()+" em "+LocalDate.now()+"."));
	            session.setAttribute("selecao", selecao);
	            session.setAttribute("etapa", etapa);
	            session.setAttribute("resultado", etapaServiceIfc.getResultado(etapa));
	            session.setAttribute("mensagem", "Etapa divulgada com sucesso!");
	            session.setAttribute("status", "success");
	            return "redirect:/selecao/" + selecao.getCodSelecao();
            } else { return "error/404";}
        }catch (IllegalCodeException e) {
    		session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
    		return "redirect:/selecao/" + codSelecao;
    	}
         
    }
    
    @RequestMapping(value="/divulgarResultadoInscricao/{codSelecao}/{codInscricao}", method = RequestMethod.GET)
    public String divulgaResultadoInscricao(@PathVariable long codSelecao, @PathVariable long codInscricao, Model model, HttpServletRequest request) throws MalformedURLException, EmailException {
    	HttpSession session = request.getSession();
    	try{
            
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
            if (selecao.getResponsaveis().contains(usuario) || usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
	            EtapaBeans etapa = etapaServiceIfc.getEtapa(codInscricao);
	            this.etapaServiceIfc.setUsuario(usuario);
	            if (etapa.getAvaliacoes() == null) {
	            	throw new Exception("Não existem avaliações para serem divulgadas!");
	            }
	            etapa.setDivulgaResultado(true);
	            etapa = etapaServiceIfc.atualizaEtapa(etapa);
	            Email email = new Email();
	            List<Thread> threadsEmail = Collections.synchronizedList(new ArrayList<Thread>());
	            for (int i =0;i < etapa.getParticipantes().size();i++) {
	            	ParticipanteBeans p = etapa.getParticipantes().get(i);
	            	threadsEmail.add(new Thread(new Email(p.getCandidato(), "Resultado de etapa divulgado!", "Resultado de etapa divulgado", "O resultado da <b>Etapa de "+etapa.getTitulo()+"</b> da <b>Seleção "+selecao.getTitulo()+"</b> foi divulgado!")));
	            	if (p.getCandidato().isRecebeEmail()) {
	            		threadsEmail.get(i).start();
	            	}
	            }
	            this.getLogServiceIfc().adicionaLog(new Log(LocalDate.now(),(UsuarioDarwin)usuario.toBusiness(), (Selecao) selecao.toBusiness(), "O(A) usuario(a) "+ usuario.getNome()+" divulgou o resultado da etapa "+etapa.getTitulo()+" na seleção "+selecao.getTitulo()+" em "+LocalDate.now()+"."));
	            session.setAttribute("selecao", selecao);
	            session.setAttribute("etapa", etapa);
	            session.setAttribute("resultado", etapaServiceIfc.getResultado(etapa));
	            session.setAttribute("mensagem", "Resultado da etapa divulgada com sucesso!");
	            session.setAttribute("status", "success");
	            return "redirect:/selecao/" + selecao.getCodSelecao();
            } else { return "error/404";}
        } catch (Exception e) {
    		session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
    		return "redirect:/selecao/" + codSelecao;
    	}
         
    }
    
    @RequestMapping(value="/removerEtapa/{codSelecao}/{codEtapa}", method = RequestMethod.GET)
    public String removerEtapa(@PathVariable long codSelecao, @PathVariable long codEtapa, Model model, HttpServletRequest request) {
    	 HttpSession session = request.getSession();
    	try{
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
            if (selecao.getResponsaveis().contains(usuario) || usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
	            this.getSelecaoServiceIfc().setUsuario(usuario);
	            EtapaBeans etapa = this.getEtapaServiceIfc().getEtapa(codEtapa);
	            List<EtapaBeans> etapas = selecao.getEtapas();
	            etapas.remove(etapa);
	            selecao.setEtapas(etapas);
	            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
	            this.getEtapaServiceIfc().removeEtapa(etapa);
	            this.getLogServiceIfc().adicionaLog(new Log(LocalDate.now(),(UsuarioDarwin)usuario.toBusiness(), (Selecao) selecao.toBusiness(), "O(A) usuario(a) "+ usuario.getNome()+" removeu a etapa "+etapa.getTitulo()+" na seleção "+selecao.getTitulo()+" em "+LocalDate.now()+"."));
	            session.setAttribute("selecao", selecao);
	            session.setAttribute("mensagem", "Etapa removida com sucesso!");
	            session.setAttribute("status", "success");
	            return "redirect:/selecao/" + selecao.getCodSelecao();
            } else {return "error/404";}
        } 
    	catch (IllegalAccessException e) {
        	session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
            return "redirect:/selecao/" + codSelecao;
        }catch (IllegalCodeException e) {
        	session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
    		return "redirect:/selecao/" + codSelecao;
    	} catch (Exception e) {
    		session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
            return "redirect:/selecao/" + codSelecao;
        }
    }

}
