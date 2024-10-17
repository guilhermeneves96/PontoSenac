package com.pontosenac.pontosenac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.pontosenac.pontosenac.services.RelatorioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    RelatorioService relatorioService;

    @GetMapping
    public ModelAndView paginaRegistro(Model model, HttpSession session) {
        return relatorioService.listarRegistrosPonto(model, session);
    }

}
