package com.pontosenac.pontosenac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pontosenac.pontosenac.model.Pessoa;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/solicitacao")
public class Solicitacao {

    @GetMapping
    public String pagina(HttpSession session, Model model) {
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        if (pessoa == null) {
            model.addAttribute("erro", "Você não esta Autenticado");
            return "redirect:/login"; // ou qualquer página de erro apropriada
        }

        return "listaSolicitacao";
    }

    @GetMapping("/novasolicitacao")
    public String formulario(HttpSession session, Model model) {
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        if (pessoa == null) {
            model.addAttribute("erro", "Você não esta Autenticado");
            return "redirect:/login";
        }
        model.addAttribute("titulo", "Nova Solicitação");
        return "novasolicitacao";
    }

    public String salvar() {
        return null;
    }

}
