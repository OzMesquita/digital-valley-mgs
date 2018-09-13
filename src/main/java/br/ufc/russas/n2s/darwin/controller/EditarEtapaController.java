/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.controller;

import br.ufc.russas.n2s.darwin.beans.EtapaBeans;
import br.ufc.russas.n2s.darwin.beans.PeriodoBeans;
import br.ufc.russas.n2s.darwin.beans.SelecaoBeans;
import br.ufc.russas.n2s.darwin.beans.UsuarioBeans;
import br.ufc.russas.n2s.darwin.model.EnumCriterioDeAvaliacao;
import br.ufc.russas.n2s.darwin.model.Periodo;
import br.ufc.russas.n2s.darwin.model.exception.IllegalCodeException;
import br.ufc.russas.n2s.darwin.service.EtapaServiceIfc;
import br.ufc.russas.n2s.darwin.service.SelecaoServiceIfc;
import br.ufc.russas.n2s.darwin.service.UsuarioServiceIfc;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Gilberto Lima
 */
@Controller("editarEtapaController")
@RequestMapping("/editarEtapa")
public class EditarEtapaController {

    private EtapaServiceIfc etapaServiceIfc;
    private SelecaoServiceIfc selecaoServiceIfc;
    private UsuarioServiceIfc usuarioServiceIfc;
    
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
           
    
    @RequestMapping(value="/{codSelecao}/{codEtapa}", method = RequestMethod.GET)
    public String getIndex(@PathVariable long codSelecao, @PathVariable long codEtapa, Model model) {
        EtapaBeans etapaBeans = this.etapaServiceIfc.getEtapa(codEtapa);
        SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
       
        List<UsuarioBeans> usuarios = this.getUsuarioServiceIfc().listaTodosUsuarios();
        List<UsuarioBeans> avaliadores = usuarioServiceIfc.listaAvaliadores();
        if (etapaBeans.getCodEtapa() == selecao.getInscricao().getCodEtapa()) {
            model.addAttribute("tipo", "inscricao"); 
        } else {
            model.addAttribute("tipo", "etapa");
        }
        model.addAttribute("selecao", selecao);
        model.addAttribute("etapa", etapaBeans);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("avaliadores", avaliadores);
        return "editar-etapa";
    }

    @RequestMapping(value="/{codSelecao}/{codEtapa}", method = RequestMethod.POST)
    public String atualiza(@PathVariable long codSelecao, @PathVariable long codEtapa, EtapaBeans etapa, @RequestParam("prerequisito") long prerequisito, BindingResult result, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	try{
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = this.selecaoServiceIfc.getSelecao(codSelecao);
            EtapaBeans etapaBeans= this.etapaServiceIfc.getEtapa(codEtapa);
            if (etapaBeans.getPeriodo().getInicio().isAfter(LocalDate.now())) {
	            String[] codAvaliadores = request.getParameterValues("codAvaliadores");
	            String[] documentosExigidos = request.getParameterValues("documentosExigidos");
	            int criterio = Integer.parseInt(request.getParameter("criterioDeAvaliacao"));
	            if (criterio == 1) {
	                etapaBeans.setCriterioDeAvaliacao(EnumCriterioDeAvaliacao.NOTA);
	            } else if(criterio == 2) {
	                etapaBeans.setCriterioDeAvaliacao(EnumCriterioDeAvaliacao.APROVACAO);
	            } else if(criterio == 3) {
	                etapaBeans.setCriterioDeAvaliacao(EnumCriterioDeAvaliacao.DEFERIMENTO);
	            }
	            etapaBeans.setTitulo(etapa.getTitulo());
	            etapaBeans.setDescricao(etapa.getDescricao());
	            etapaBeans.setPrerequisito(etapa.getPrerequisito());
	            
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	            etapaBeans.setPeriodo(new PeriodoBeans(0, LocalDate.parse(request.getParameter("dataInicio"), formatter), LocalDate.parse(request.getParameter("dataTermino"), formatter)));
	            //Verificando se houve conflito com as outras Etapas.
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
	                for(String documento : documentosExigidos){
	                    docs.add(documento);
	                }
	                etapaBeans.setDocumentacaoExigida(docs);
	            }
	            etapaBeans.setAvaliadores(avaliadores);
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
	            //this.etapaServiceIfc.atualizaEtapa(selecao, etapaBeans);
	            model.addAttribute("selecao", selecao);
	            return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
            } else {
            	model.addAttribute("selecao", selecao); 
            	return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
            }
        } catch (IllegalAccessException e) {
        	session.setAttribute("mensagem", e.getMessage());
        	session.setAttribute("status", "danger");
            return "redirect:/editarEtapa/" + codSelecao+"/"+codEtapa;
        }catch (IllegalCodeException e) {
        	session.setAttribute("mensagem", "Etapa não pode ser atualizada! Verifique o conflito entre periodos com outras etapas!");
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
            if (inscricaoBeans.getPeriodo().getInicio().isAfter(LocalDate.now())) {
	            String[] codAvaliadores = request.getParameterValues("codAvaliadores");
	            String[] documentosExigidos = request.getParameterValues("documentosExigidos");
	            inscricaoBeans.setTitulo(inscricao.getTitulo());
	            inscricaoBeans.setDescricao(inscricao.getDescricao());
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	            inscricaoBeans.setPeriodo(new PeriodoBeans(0, LocalDate.parse(request.getParameter("dataInicio"), formatter), LocalDate.parse(request.getParameter("dataTermino"), formatter)));
	            List <EtapaBeans> subsequentes = selecao.getEtapas();
	            Periodo novoP = (Periodo) inscricaoBeans.getPeriodo().toBusiness();
	            for(EtapaBeans sub: subsequentes){
	            	if(sub.getCodEtapa()!=inscricao.getCodEtapa()) {
	            		Periodo periodo =(Periodo) sub.getPeriodo().toBusiness();
	            		if(periodo.isColide(novoP)) {
	            			throw new IllegalCodeException("Periodo Inválido!");
	            		}
	            		}
	            }
	            ArrayList<UsuarioBeans> avaliadores = new ArrayList<>();
	            try {
		            if (codAvaliadores != null) {
		                for (String cod : codAvaliadores) {
		                    UsuarioBeans u = this.getUsuarioServiceIfc().getUsuario(Long.parseLong(cod),0);
		                    if (u != null) {
		                        avaliadores.add(u);
		                    }
		                }
		            }
	            } catch (NumberFormatException e) {
	            	model.addAttribute("mensagem", "Avaliador(es) inválido(s)!");
	                model.addAttribute("status", "error");
	            	return "redirect:/editarEtapa/" + codSelecao+"/"+codInscricao;
	            }
	            if (documentosExigidos != null) {
	                ArrayList<String> docs = new ArrayList<>();
	                for (String documento : documentosExigidos) {
	                    docs.add(documento);
	                }
	                inscricaoBeans.setDocumentacaoExigida(docs);
	            }
	            inscricaoBeans.setAvaliadores(avaliadores);
	            this.getSelecaoServiceIfc().setUsuario(usuario);
	            selecao.setInscricao(inscricaoBeans);
	            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
	            session.setAttribute("selecao", selecao);
	            session.setAttribute("mensagem", "Etapa atualizada com sucesso!");
	            session.setAttribute("status", "success");
	            return "redirect:/editarEtapa/" + selecao.getCodSelecao()+"/"+codInscricao;
            } else {
            	session.setAttribute("selecao", selecao);
            	session.setAttribute("mensagem", "Etapa iniciada, não é mais possível realizar edições!");
	            session.setAttribute("status", "error");
	            return "redirect:/editarEtapa/" + selecao.getCodSelecao()+"/"+codInscricao;
            }
        }catch (IllegalCodeException e) {
        	session.setAttribute("mensagem", "Etapa não pode ser atualizada! Verifique o conflito entre periodos com outras etapas!");
            session.setAttribute("status", "warning");
    		return "redirect:/editarEtapa/" +codSelecao+"/"+codInscricao;
    	}catch (IllegalAccessException e) {
    		session.setAttribute("mensagem", e.getMessage());
            session.setAttribute("status", "danger");
    		return "redirect:/editarEtapa/" +codSelecao+"/"+codInscricao;
    	}
         
    }
    
    @RequestMapping(value="/divulgarResultado/{codSelecao}/{codInscricao}", method = RequestMethod.GET)
    public String divulgaResultado(@PathVariable long codSelecao, @PathVariable long codEtapa, EtapaBeans etapa, BindingResult result, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	try{
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
            etapa = etapaServiceIfc.getEtapa(codEtapa);
            etapa.setDivulgaResultado(true);
            int index=0;
            this.getSelecaoServiceIfc().setUsuario(usuario);
            List<EtapaBeans> etapas = selecao.getEtapas();
            for(EtapaBeans etapaIterator : etapas) {
            	if(etapaIterator.getCodEtapa()==etapa.getCodEtapa()) {
            		selecao.getEtapas().remove(etapaIterator);
            		selecao.getEtapas().add(index, etapa);
            		break;
            	}
            	index++;
            }
            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
            session.setAttribute("selecao", selecao);
            session.setAttribute("mensagem", "Etapa divulgada com sucesso!");
            session.setAttribute("status", "success");
            return "redirect:/selecao/" + selecao.getCodSelecao();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "redirect:/selecao/" + codSelecao;
        }catch (IllegalCodeException e) {
    		e.printStackTrace();
    		return "redirect:/selecao/" + codSelecao;
    	}
         
    }
    
    @RequestMapping(value="/divulgarResultadoInscricao/{codSelecao}/{codInscricao}", method = RequestMethod.GET)
    public String divulgaResultadoInscricao(@PathVariable long codSelecao, @PathVariable long codInscricao, Model model, HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
            this.getSelecaoServiceIfc().setUsuario(usuario);  
            selecao.getInscricao().setDivulgaResultado(true);
            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
            session.setAttribute("selecao", selecao);
            session.setAttribute("mensagem", "Resultado da etapa divulgada com sucesso!");
            session.setAttribute("status", "success");
            return "redirect:/selecao/" + selecao.getCodSelecao();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "redirect:/selecao/" + codSelecao;
        }catch (IllegalCodeException e) {
    		e.printStackTrace();
    		return "redirect:/selecao/" + codSelecao;
    	}
         
    }
    
    @RequestMapping(value="/removerEtapa/{codSelecao}/{codEtapa}", method = RequestMethod.GET)
    public String removerEtapa(@PathVariable long codSelecao, @PathVariable long codEtapa, Model model, HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
            SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
            this.getSelecaoServiceIfc().setUsuario(usuario);
            EtapaBeans etapa = this.getEtapaServiceIfc().getEtapa(codEtapa);
            
          /*  if (recebido instanceof InscricaoBeans) {
            	selecao.setInscricao(null);
            	selecao.setDivulgada(false);
            	this.getEtapaServiceIfc().removeInscricao((InscricaoBeans)recebido);
            }
            EtapaBeans etapa = (EtapaBeans) recebido;*/
            List<EtapaBeans> etapas = selecao.getEtapas();
            etapas.remove(etapa);
            selecao.setEtapas(etapas);
            selecao = this.getSelecaoServiceIfc().atualizaSelecao(selecao);
            this.getEtapaServiceIfc().removeEtapa(etapa);
            session.setAttribute("selecao", selecao);
            session.setAttribute("mensagem", "Etapa removida com sucesso!");
            session.setAttribute("status", "success");
            return "redirect:/selecao/" + selecao.getCodSelecao();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "redirect:/selecao/" + codSelecao;
        }catch (IllegalCodeException e) {
    		e.printStackTrace();
    		return "redirect:/selecao/" + codSelecao;
    	}
         
    }
    
    
    

}
