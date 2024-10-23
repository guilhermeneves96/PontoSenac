package com.pontosenac.pontosenac.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.repository.PessoaRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {

    @Autowired
    PessoaRepository pessoaRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String paginaLogin() {
        return "login";
    }

    public String validarAcesso(Pessoa pessoa, Model model, RedirectAttributes redirect, HttpSession session) {

        Optional<Pessoa> pessoaOpt = pessoaRepository.findByMatricula(pessoa.getMatricula());
        if (pessoaOpt.isPresent()) {

            Pessoa p = pessoaOpt.get();

            if (passwordEncoder.matches(pessoa.getSenha(), p.getSenha())) {

                session.setAttribute("pessoaAutenticada", p);

                return p.getPerfil().getPermissao().equalsIgnoreCase("admin") ? "redirect:/inicio"
                        : "redirect:/baterponto";
            }
        }
        redirect.addFlashAttribute("erro", "Matrícula ou senha inválida");
        return "redirect:/login";

    }

}
