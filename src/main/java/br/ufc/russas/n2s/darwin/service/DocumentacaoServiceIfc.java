/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.service;

import br.ufc.russas.n2s.darwin.beans.DocumentacaoBeans;
import java.util.List;

/**
 *
 * @author Wallison Carlos
 */
public interface DocumentacaoServiceIfc {

    /**
     *
     * @param documentacao
     * Parâmetros
     * documentacao - Uma nova documentação a ser armazenada
     * @return DocumentacaoBeans
     */
    DocumentacaoBeans adicionaDocumentacao(DocumentacaoBeans documentacao);

    /**
     *
     * @param documentacao
     * Parâmetros
     * documentacao - Uma documentação para ser atualizada
     * @return DocumentacaoBeans
     */
    DocumentacaoBeans atualizaDocumentacao(DocumentacaoBeans documentacao);

    /**
     *
     * @param documentacao
     * Parâmetros
     * documentacao - Uma documentação para ser removida
     */
    void removeDocumentacao(DocumentacaoBeans documentacao);

    /**
     *
     * @return List
     */
    List<DocumentacaoBeans> listaTodasDocumentacoes();

    /**
     *
     * @param codDocumentacao
     * codDocumentacao - Identificador único da documentação a ser buscada
     * @return DocumentacaoBeans
     */
    DocumentacaoBeans getDocumentacao(long codDocumentacao);
}
