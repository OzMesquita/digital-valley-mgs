/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.beans;

import br.ufc.russas.n2s.darwin.model.Usuario;

/**
 *
 * @author Lavínia Matoso
 */
public class UsuarioBeans implements Beans{
    

    @Override
    public Object toBusiness() {
        return new Usuario();
    }

    @Override
    public Beans toBeans(Object object) {
        return new UsuarioBeans();  
    }
    
}
