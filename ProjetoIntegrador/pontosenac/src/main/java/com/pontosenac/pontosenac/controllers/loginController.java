package com.pontosenac.pontosenac.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.repository.PessoaRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/login")
public class loginController {

    @Autowired
    PessoaRepository pessoaRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping("/validar")
    public String Acesso(Pessoa pessoa, Model model, RedirectAttributes redirect, HttpSession session) {

        Optional<Pessoa> pessoaOpt = pessoaRepository.findByMatricula(pessoa.getMatricula());

        if (pessoaOpt.isPresent()) {
            Pessoa p = pessoaOpt.get();
            if (passwordEncoder.matches(pessoa.getSenha(), p.getSenha())) {
                // Armazenar a pessoa autenticada na sessão
                session.setAttribute("pessoaAutenticada", p);

                return p.getPerfil().getPermissao().equalsIgnoreCase("admin") ? "redirect:/inicio"
                        : "redirect:/baterponto";
            }
        }

        // Caso não encontre a pessoa ou senha não coincida
        redirect.addFlashAttribute("erro", "Matrícula ou senha inválida");
        return "redirect:/login";
    }

}
