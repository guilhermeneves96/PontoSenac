package com.pontosenac.pontosenac.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ch.qos.logback.core.model.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pontosenac.pontosenac.model.Funcao;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.TipoSolicitacao;
import com.pontosenac.pontosenac.repository.FuncaoRepository;
import com.pontosenac.pontosenac.repository.PessoaRepository;
import com.pontosenac.pontosenac.repository.TipoSolicitacaoRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ConfiguracaoService {

    @Autowired
    PessoaRepository pessoaRepository;
    @Autowired
    FuncaoRepository funcaoRepository;
    @Autowired
    TipoSolicitacaoRepository tipoSolicitacaoRepository;

    public ModelAndView paginaConfig(HttpSession session, Model model, RedirectAttributes redirect) {
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");

        return pessoa.getPerfil().getPermissao().equalsIgnoreCase("admin") ? paginaConfigAdmin(session, model, redirect)
                : paginaConfiUsuario(session, model, redirect);

    }

    public ModelAndView paginaConfiUsuario(HttpSession session, Model model, RedirectAttributes redirect) {

        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        ModelAndView mv = new ModelAndView("configUsuario");

        if (pessoa == null) {
            redirect.addFlashAttribute("erro", "Matrícula ou senha inválida");
            mv.setViewName("redirect:/login");
        } else {

            Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoa.getId());
            mv.addObject("pessoa", pessoaOpt);

        }

        return mv;
    }

    public ModelAndView paginaConfigAdmin(HttpSession session, Model model, RedirectAttributes redirect) {

        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        ModelAndView mv = new ModelAndView("configAdmin");

        if (pessoa == null) {
            redirect.addFlashAttribute("erro", "Matrícula ou senha inválida");
            mv.setViewName("redirect:/login");
        } else {

            List<Funcao> funcoes = funcaoRepository.findAll();
            mv.addObject("funcoes", funcoes);

            List<TipoSolicitacao> tipoSolicitacaos = tipoSolicitacaoRepository.findAll();
            mv.addObject("tipoSolicitacaos", tipoSolicitacaos);

            Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoa.getId());
            mv.addObject("pessoa", pessoaOpt);

        }

        return mv;

    }

}
