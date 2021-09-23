/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.service;

import br.ufc.russas.n2s.darwin.beans.DocumentacaoBeans;
import br.ufc.russas.n2s.darwin.dao.DocumentacaoDAOIfc;
import br.ufc.russas.n2s.darwin.model.Documentacao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Wallison Carlos
 */
public class DocumentacaoServiceImpl implements DocumentacaoServiceIfc {

    private DocumentacaoDAOIfc documentacaoDAOIfc;

    public DocumentacaoDAOIfc getDocumentacaoDAOIfc() {
        return documentacaoDAOIfc;
    }

    @Autowired(required = true)
    public void setEtapaDAOIfc(@Qualifier("documentacaoDAOIfc")DocumentacaoDAOIfc documentacaoDAOIfc) {
        this.documentacaoDAOIfc = documentacaoDAOIfc;
    }

    @Override
    public DocumentacaoBeans adicionaDocumentacao(DocumentacaoBeans documentacao) {
        Documentacao doc = (Documentacao) documentacao.toBusiness();
        return (DocumentacaoBeans) new DocumentacaoBeans().toBeans(this.getDocumentacaoDAOIfc().adicionaDocumentacao(doc));
    }

    @Override
    public DocumentacaoBeans atualizaDocumentacao(DocumentacaoBeans documentacao) {
        Documentacao doc = (Documentacao) documentacao.toBusiness();
        return (DocumentacaoBeans) new DocumentacaoBeans().toBeans(this.getDocumentacaoDAOIfc().atualizaDocumentacao(doc));
    }

    @Override
    public void removeDocumentacao(DocumentacaoBeans documentacao) {
        this.getDocumentacaoDAOIfc().removeDocumentacao((Documentacao) documentacao.toBusiness());
    }

    @Override
    public List<DocumentacaoBeans> listaTodasDocumentacoes() {
        Documentacao doc = new Documentacao();
        List<DocumentacaoBeans> documentacoes = Collections.synchronizedList(new ArrayList<DocumentacaoBeans>());
        List<Documentacao> result = this.getDocumentacaoDAOIfc().listaDocumentacoes(doc);
        for (Documentacao documentacao : result) {
            documentacoes.add((DocumentacaoBeans) new DocumentacaoBeans().toBeans(documentacao));
        }
        return documentacoes;
    }

    @Override
    public DocumentacaoBeans getDocumentacao(long codDocumentacao) {
        Documentacao doc = new Documentacao();
        doc.setCodDocumentacao(codDocumentacao);
        return (DocumentacaoBeans) new DocumentacaoBeans().toBeans(this.getDocumentacaoDAOIfc().getDocumentacao(doc));
    }

}
