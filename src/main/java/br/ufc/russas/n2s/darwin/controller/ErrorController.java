/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Wallison Carlos
 */
@Controller("errorController")
public class ErrorController {
    
    String path = "/error";
    
    @RequestMapping(value="/400")
    public String error400(){
     System.out.println("custom error handler");
     return path+"/400";
    }

    @RequestMapping(value="/404")
    public String error404(){
     System.out.println("custom error handler");
     return path+"/404";
    }

    @RequestMapping(value="/500")
    public String error500(){
     System.out.println("custom error handler");
     return path+"/500";
    }
}
