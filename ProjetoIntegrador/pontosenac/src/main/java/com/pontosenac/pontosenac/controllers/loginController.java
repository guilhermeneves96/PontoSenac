package com.pontosenac.pontosenac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.services.LoginService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/login")
public class loginController {

    @Autowired
    LoginService loginService;

    @GetMapping
    public String login() {
        return loginService.paginaLogin();
    }

    @PostMapping("/validar")
    public String Acesso(Pessoa pessoa, Model model, RedirectAttributes redirect, HttpSession session) {
        return loginService.validarAcesso(pessoa, model, redirect, session);
    }
}
