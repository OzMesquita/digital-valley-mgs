package br.ufc.russas.n2s.darwin.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

import br.ufc.russas.n2s.darwin.beans.AvaliacaoBeans;
import br.ufc.russas.n2s.darwin.beans.EtapaBeans;
import br.ufc.russas.n2s.darwin.beans.ParticipanteBeans;
import br.ufc.russas.n2s.darwin.beans.ResultadoParticipanteSelecaoBeans;
import br.ufc.russas.n2s.darwin.beans.SelecaoBeans;
import br.ufc.russas.n2s.darwin.beans.UsuarioBeans;
import br.ufc.russas.n2s.darwin.dao.AvaliacaoDAOIfc;
import br.ufc.russas.n2s.darwin.model.Avaliacao;
import br.ufc.russas.n2s.darwin.model.EnumPermissao;
import br.ufc.russas.n2s.darwin.model.Etapa;
import br.ufc.russas.n2s.darwin.model.Log;
import br.ufc.russas.n2s.darwin.model.Selecao;
import br.ufc.russas.n2s.darwin.model.UsuarioDarwin;
import br.ufc.russas.n2s.darwin.service.AvaliacaoServiceIfc;
import br.ufc.russas.n2s.darwin.service.EtapaServiceIfc;
import br.ufc.russas.n2s.darwin.service.LogServiceIfc;
import br.ufc.russas.n2s.darwin.service.SelecaoServiceIfc;

/**
 *
 * @author Wallison Carlos
 */
@Controller("selecaoController")
@RequestMapping(value = "/selecao")
public class SelecaoController {
    
    private SelecaoServiceIfc selecaoServiceIfc;
    private EtapaServiceIfc etapaServiceIfc;
    private LogServiceIfc logServiceIfc;
    
    @Autowired(required = true)
    public void setSelecaoServiceIfc(@Qualifier("selecaoServiceIfc")SelecaoServiceIfc selecaoServiceIfc){
        this.selecaoServiceIfc = selecaoServiceIfc;
    }
    
    @Autowired(required = true)
    public void setEtapaServiceIfc(@Qualifier("etapaServiceIfc")EtapaServiceIfc etapaServiceIfc) {
    	this.etapaServiceIfc = etapaServiceIfc;
    }
    
    public LogServiceIfc getLogServiceIfc() {
    	return logServiceIfc;
    }
    @Autowired
    public void setLogServiceIfc(@Qualifier("logServiceIfc")LogServiceIfc logServiceIfc) {
    	this.logServiceIfc = logServiceIfc;
    }

    @RequestMapping(value = "/{codSelecao}", method = RequestMethod.GET)
    public String getIndex(@PathVariable long codSelecao, Model model, HttpServletRequest request){
        SelecaoBeans selecao = this.selecaoServiceIfc.getSelecao(codSelecao);
        HttpSession session = request.getSession();
        UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
        HashMap<Long, List<UsuarioBeans>> classificados = new HashMap<>();
        for(EtapaBeans et : selecao.getEtapas()) {
        	List<UsuarioBeans> usuarios = Collections.synchronizedList(new ArrayList<UsuarioBeans>());
        	for (ParticipanteBeans pb : et.getParticipantes()) {
        		usuarios.add(pb.getCandidato());
        	}
        	classificados.put(et.getCodEtapa(), usuarios);
        }
        
        
        boolean isParticipante = false;
        boolean isResponsavel = false;
        if (selecao.getInscricao() != null && selecao.getInscricao().getParticipantes() != null) {
        	for (ParticipanteBeans participante : selecao.getInscricao().getParticipantes()) {
    			if (participante.getCandidato().getCodUsuario() == usuario.getCodUsuario()) {
    				isParticipante = true;
    			}
    		}
        }
        if (selecao.getResponsaveis().contains(usuario) || usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
        	isResponsavel = true;
        }
        
        selecao.setEtapas(this.etapaServiceIfc.ordenaEtapasPorData(selecao.getEtapas()));
        model.addAttribute("isParticipante", isParticipante);
        if (!selecao.isDivulgada()) {
            model.addAttribute("selecao", selecao);        
            model.addAttribute("etapaAtual", this.selecaoServiceIfc.getEtapaAtual(selecao));
            model.addAttribute("isResponsavel", isResponsavel);
            session.setAttribute("selecao", selecao);
            return "selecao";
        } else if(selecao.isDivulgada()) {
            HashMap<EtapaBeans, Object[]> situacao = new HashMap<>();
            int i = 0;
            Etapa etapabusiness = (Etapa) etapaServiceIfc.getEtapa(selecao.getInscricao().getCodEtapa()).toBusiness();
            selecao.getInscricao().getAvaliacoes().removeAll(selecao.getInscricao().getAvaliacoes());
            for(Avaliacao avaliacao:etapabusiness.getAvaliacoes()) {
            	AvaliacaoBeans avli = new AvaliacaoBeans();
        		avli.toBeans(avaliacao);
        		selecao.getInscricao().getAvaliacoes().add(avli);
            }
            situacao.put(selecao.getInscricao(), this.etapaServiceIfc.getSituacao(selecao.getInscricao(), usuario));
            for (i = 0; i<selecao.getEtapas().size(); i++) {
            	EtapaBeans etapa = (EtapaBeans) selecao.getEtapas().get(i);
            	etapabusiness = (Etapa) etapaServiceIfc.getEtapa(etapa.getCodEtapa()).toBusiness();
            	etapa.getAvaliacoes().removeAll(etapa.getAvaliacoes());
            	selecao.getEtapas().remove(i);
            	selecao.getEtapas().add(i, etapa);
                situacao.put(etapa, this.etapaServiceIfc.getSituacao(etapa, usuario));
                i++;
            }
            model.addAttribute("classificados", classificados);
            model.addAttribute("situacao", situacao);
            model.addAttribute("selecao", selecao);        
            model.addAttribute("etapaAtual", this.selecaoServiceIfc.getEtapaAtual(selecao));
            session.setAttribute("selecao", selecao);
            model.addAttribute("isResponsavel", isResponsavel);
            return "selecao";
        } else {
            return "elements/error404";
        }
    }
    
    @RequestMapping(value = "/editar-selecao/{codSelecao}", method = RequestMethod.GET)
    public String remove(@PathVariable String selecaoCodigo, SelecaoBeans selecao, Model model, BindingResult result, HttpServletRequest request){
        this.selecaoServiceIfc.removeSelecao(selecao);
        request.getSession().setAttribute("selecao", selecao);
        return "selecao";
    }
  
    @RequestMapping(value = "/{codSelecao}/resultado", method = RequestMethod.GET)
    public String resultado(@PathVariable long codSelecao, Model model, HttpServletRequest request) {
    	SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
    	UsuarioBeans usuario = (UsuarioBeans) request.getSession().getAttribute("usuarioDarwin");
    	if (selecao.isDivulgadoResultado() || (selecao.getResponsaveis().contains(usuario)) || (usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR))) {
	    	try {
	    		List<ResultadoParticipanteSelecaoBeans> resultado = selecaoServiceIfc.getResultado(selecao);
	    		List<EtapaBeans> etapasComNota = selecaoServiceIfc.getEtapasNota(selecao);
	    		model.addAttribute("resultadosSelecao", resultado);
	    		if (!resultado.isEmpty()) {
	    			etapasComNota = resultado.get(0).getEtapas();
	    		}
	    		if (selecao.getResponsaveis().contains(usuario) || usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR)) {
	            	model.addAttribute("isResponsavel", true);
	            } else {
	            	model.addAttribute("isResponsavel", false);
	            }
		        model.addAttribute("etapasComNota", etapasComNota);
		        model.addAttribute("selecao", selecao);
		        model.addAttribute("etapa", selecaoServiceIfc.getUltimaEtapa(selecao));
		        return "resultado";
	    	} catch (NullPointerException e) {
				model.addAttribute("mensagem", "Não foram encontrados resultados disponíveis!");
	            model.addAttribute("status", "warning");
	            return "resultado";
			} catch (Exception e) {
	 	        model.addAttribute("quantidadeEtapasPorNota", selecaoServiceIfc.getEtapasNota(selecao).size());
	 	        model.addAttribute("selecao", selecao);
	 	        model.addAttribute("etapa", selecaoServiceIfc.getUltimaEtapa(selecao));
	 	        return "resultado";
	     	}
    	} else { return "error/404";}
    }
    
    @RequestMapping(value = "/{codSelecao}/participantes", method = RequestMethod.GET)
    public String getParticipantesInscricao(@PathVariable long codSelecao, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
    	UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
     
        if ((selecao.getResponsaveis().contains(usuario)) || (usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR))) {
	        model.addAttribute("selecao", selecao);
	        model.addAttribute("participantesEtapa", selecao.getInscricao().getParticipantes());
	        return "participantes";
        } else {return "error/404";}
    }
    
        
    //Adicao do novo metodo de remover
    @RequestMapping(value = "/{codSelecao}/remover", method = RequestMethod.GET)
    public String removerSelecao(@PathVariable long codSelecao, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	SelecaoBeans selecao = selecaoServiceIfc.getSelecao(codSelecao);
    	UsuarioBeans usuario = (UsuarioBeans) session.getAttribute("usuarioDarwin");
    	if (!selecao.isDivulgadoResultado() && (selecao.getResponsaveis().contains(usuario)) || (usuario.getPermissoes().contains(EnumPermissao.ADMINISTRADOR))) {
    		try {
    			selecaoServiceIfc.setUsuario(usuario);
    			selecaoServiceIfc.removeSelecao(selecao);
    			this.getLogServiceIfc().adicionaLog(new Log(LocalDate.now(),(UsuarioDarwin)usuario.toBusiness(), (Selecao) selecao.toBusiness(), "O(A) usuario(a) "+ usuario.getNome()+" excluiu a seleção "+selecao.getTitulo()+" em "+LocalDate.now()+"."));
    			session.setAttribute("mensagem", "Seleção excluida com sucesso");
    			session.setAttribute("status", "success");
    			return ("redirect:/");
    		} catch( Exception e) {
    			model.addAttribute("mensagem", "Não foi possível excluir esta seleção!");
	            model.addAttribute("status", "danger");
	            return "selecao";
    		}
    		
    	} else { return "error/404";}
    	
    	
  
    }
    
}
