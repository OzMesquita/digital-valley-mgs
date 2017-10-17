/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufc.russas.n2s.darwin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 *
 * @author Wallison Carlos
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping(method = {RequestMethod.GET})
    public String handle(Model model) {
            model.addAttribute("message", "Hello World!");
            return "hello";
    }
}
