package com.pontosenac.pontosenac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.pontosenac.pontosenac.services.BaterPontoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/baterponto")
public class BaterpontoController {

    @Autowired
    BaterPontoService baterPontoService;

    @GetMapping
    public ModelAndView pagina(Model model, HttpSession session) {
        return baterPontoService.pagina(model, session);
    }

    @PostMapping
    public String baterPonto(Model model, HttpSession session) {
        return baterPontoService.baterPonto(model, session);
    }

}