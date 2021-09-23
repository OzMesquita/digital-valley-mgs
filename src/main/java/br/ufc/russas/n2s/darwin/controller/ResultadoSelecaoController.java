package br.ufc.russas.n2s.darwin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.ufc.russas.n2s.darwin.beans.EtapaBeans;
import br.ufc.russas.n2s.darwin.beans.ResultadoParticipanteSelecaoBeans;
import br.ufc.russas.n2s.darwin.beans.SelecaoBeans;
import br.ufc.russas.n2s.darwin.beans.UsuarioBeans;
import br.ufc.russas.n2s.darwin.model.EnumPermissao;
import br.ufc.russas.n2s.darwin.model.ResultadoSelecaoForm;
import br.ufc.russas.n2s.darwin.service.EtapaServiceIfc;
import br.ufc.russas.n2s.darwin.service.SelecaoServiceIfc;
import br.ufc.russas.n2s.darwin.service.UsuarioServiceIfc;
import br.ufc.russas.n2s.darwin.util.Facade;

@Controller("resultadoSelecaoController")
@RequestMapping("/resultadoSelecao")
public class ResultadoSelecaoController {
	private SelecaoServiceIfc selecaoServiceIfc;
	private EtapaServiceIfc etapaServiceIfc;
    private UsuarioServiceIfc usuarioServiceIfc;
    
    
    public SelecaoServiceIfc getSelecaoServiceIfc() {
        return selecaoServiceIfc;
    }
    @Autowired(required = true)
    public void setSelecaoSelecaoServiceIfc(@Qualifier("selecaoServiceIfc")SelecaoServiceIfc selecaoServiceIfc) {
        this.selecaoServiceIfc = selecaoServiceIfc;
    }
    
    public EtapaServiceIfc getEtapaServiceIfc() {
        return etapaServiceIfc;
    }
    @Autowired(required = true)
    public void setEtapaServiceIfc(@Qualifier("etapaServiceIfc")EtapaServiceIfc etapaServiceIfc) {
        this.etapaServiceIfc = etapaServiceIfc;
    }
    
    
    public UsuarioServiceIfc getUsuarioServiceIfc() {
        return usuarioServiceIfc;
    }
    @Autowired(required = true)
    public void setUsuarioServiceIfc(@Qualifier("usuarioServiceIfc")UsuarioServiceIfc usuarioServiceIfc) {
        this.usuarioServiceIfc = usuarioServiceIfc;
    }
    
	@RequestMapping(value = "/{codSelecao}", method = RequestMethod.GET)
    public String getResultadoDaselecao(@PathVariable long codSelecao, Model model, HttpServletRequest request){
		UsuarioBeans usuario = (UsuarioBeans) request.getSession().getAttribute("usuarioDarwin");
        SelecaoBeans selecao  = this.getSelecaoServiceIfc().getSelecao(codSelecao);
        if (!selecao.getResponsaveis().contains(usuario) && !usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
        	return "error/404";
        }
        ResultadoSelecaoForm resultadoForm = new ResultadoSelecaoForm();
        resultadoForm.setEtapas( this.getSelecaoServiceIfc().getEtapasNota(selecao));
        
        model.addAttribute("resultadoSelecaoForm", resultadoForm);
        return "calculo-resultado-selecao";
    }
	
	
	
	@RequestMapping(value = "/salvar/{codSelecao}", method = RequestMethod.POST)
    public String calculaResultadoDaselecao(@PathVariable long codSelecao, Model model, HttpServletRequest request, @ModelAttribute("resultadoSelecaoForm") ResultadoSelecaoForm resultadoForm) {
        SelecaoBeans selecao  = selecaoServiceIfc.getSelecao(codSelecao);
        UsuarioBeans usuario = (UsuarioBeans) request.getSession().getAttribute("usuarioDarwin");
        if (selecao.getResponsaveis().contains(usuario) || usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
	        this.getEtapaServiceIfc().setUsuario(usuario);
	        this.getSelecaoServiceIfc().setUsuario(usuario);
	
	        List<EtapaBeans> etapas = new ArrayList<>();
	        etapas = resultadoForm.getEtapas();
	        EtapaBeans etapaAux;
	        if (etapas != null && etapas.size() > 0) {
	        	for (EtapaBeans eb : etapas) {
	        		etapaAux = this.getEtapaServiceIfc().getEtapa(eb.getCodEtapa());
	        		etapaAux.setPesoNota(eb.getPesoNota());
	        		etapaAux.setPosicaoCriterioDesempate(eb.getPosicaoCriterioDesempate());
	        		etapaAux.setCriterioDesempate(eb.isCriterioDesempate());     		
	        		this.getEtapaServiceIfc().atualizaEtapa(etapaAux);      		
	        	}
	        }
	                
	        selecao = this.getSelecaoServiceIfc().getSelecao(selecao.getCodSelecao());
	        resultadoForm.setEtapas(this.getSelecaoServiceIfc().getEtapasNota(selecao));
	        model.addAttribute("resultadoSelecaoForm", resultadoForm);      
	        model.addAttribute("status", "success");
	        model.addAttribute("mensagem", "Cálculo do resultado definido com sucesso!");
	        return "calculo-resultado-selecao";
        } else {return "error/404";}
    }
	
    @RequestMapping(value = "/{codSelecao}/divulgaResultado", method = RequestMethod.GET)
    public String divulgaResultadoSelecao(@PathVariable long codSelecao, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
    	UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
    	if (!selecao.isDivulgadoResultado() && ((selecao.getResponsaveis().contains(usuario)) || (usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)))) {
	    	try {
	    		selecao.setDivulgadoResultado(true);
	    		selecaoServiceIfc.setUsuario(usuario);
	    		selecaoServiceIfc.divulgaResultadoSelecao(selecao);
	    		session.setAttribute("mensagem","Resultado final divulgado com sucesso!");
		        selecao = selecaoServiceIfc.getSelecao(selecao.getCodSelecao());
	    		session.setAttribute("selecao", selecao);
		        session.setAttribute("status","success");
		        return ("redirect:/selecao/"+selecao.getCodSelecao());
	    	} catch (NullPointerException e) {
				model.addAttribute("mensagem", "Não foi possivel divulgar o resultado!");
	            model.addAttribute("status", "danger");
	            model.addAttribute("quantidadeEtapasPorNota", selecaoServiceIfc.getEtapasNota(selecao).size());
	 	        model.addAttribute("selecao", selecao);
	 	        model.addAttribute("etapa", selecaoServiceIfc.getUltimaEtapa(selecao));
	            return "resultado";
			} catch (Exception e) {
				model.addAttribute("mensagem", "Não foi possivel divulgar o resultado!");
	            model.addAttribute("status", "danger");
	 	        model.addAttribute("quantidadeEtapasPorNota", selecaoServiceIfc.getEtapasNota(selecao).size());
	 	        model.addAttribute("selecao", selecao);
	 	        model.addAttribute("etapa", selecaoServiceIfc.getUltimaEtapa(selecao));
	 	        return "resultado";
	     	}
    	} else { return "error/404";}
    }
	
	
	@RequestMapping(value = "/{codSelecao}/imprimir", method = RequestMethod.GET)
    public String imprimirResultadoDaSelecao(@PathVariable long codSelecao, Model model, HttpServletRequest request, HttpServletResponse response){
        SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
        try {
        	List<ResultadoParticipanteSelecaoBeans> resultado = (List<ResultadoParticipanteSelecaoBeans>)selecaoServiceIfc.getResultado(selecao);
	        String caminho = Facade.gerarPDFResultadoSelecao(selecao, resultado);
	        File file = new File(caminho);
	        response.setContentType("application/pdf");
	        response.addHeader("Content-Disposition", "attachment; filename=" + file.getName()+".pdf");
	        response.setContentLength((int) file.length());
	        FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
	        OutputStream responseOutputStream = response.getOutputStream();
	        int bytes;
	        while ((bytes = fileInputStream.read()) != -1) {
	                responseOutputStream.write(bytes);
	        }
	        fileInputStream.close();
	        responseOutputStream.flush();
	        responseOutputStream.close();
	        response.flushBuffer();
	        
        } catch (FileNotFoundException e) {
        	model.addAttribute("mensagem", "Arquivo não pode ser gerado!");
            model.addAttribute("status", "danger");
            return "redirect:/selecao/"+selecao.getCodSelecao()+"/resultado";
		} catch (NullPointerException e) {
			model.addAttribute("mensagem", "Arquivo não pode ser gerado!");
            model.addAttribute("status", "danger");
            return "redirect:/selecao/"+selecao.getCodSelecao()+"/resultado";
		}
        catch (IllegalArgumentException | IllegalAccessException e) {
            model.addAttribute("mensagem", e.getMessage());
            model.addAttribute("status", "danger");
            return "redirect:/selecao/"+selecao.getCodSelecao()+"/resultado";
		} catch ( Exception e) {
			model.addAttribute("mensagem", e.getMessage());
            model.addAttribute("status", "danger");
            return "redirect:/selecao/"+selecao.getCodSelecao()+"/resultado";
		}
        model.addAttribute("mensagem", "Resultado gerado com sucesso!");
        model.addAttribute("status", "success");
        return "redirect:/selecao/"+selecao.getCodSelecao()+"/resultado";
    }
	
}