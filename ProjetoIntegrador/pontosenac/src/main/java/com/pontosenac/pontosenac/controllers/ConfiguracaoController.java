package com.pontosenac.pontosenac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ch.qos.logback.core.model.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pontosenac.pontosenac.services.ConfiguracaoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/configuracao")
public class ConfiguracaoController {

    @Autowired
    ConfiguracaoService configuracaoService;

    @GetMapping
    public ModelAndView paginaConfig(HttpSession session, Model model, RedirectAttributes redirect) {
        return configuracaoService.paginaConfig(session, model, redirect);
    }
}
