package com.pontosenac.pontosenac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pontosenac.pontosenac.model.Pessoa;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/configuracaoUsuario")
public class ConfiguracaoUsuario {

    @GetMapping
    public String paginaConfig(Model model, HttpSession session) {
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        model.addAttribute("colaborador", pessoa);
        return "configUsuario";

    }
}
