package com.pontosenac.pontosenac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.pontosenac.pontosenac.model.Solicitacao;
import com.pontosenac.pontosenac.repository.SolicitacoesRepository;
import com.pontosenac.pontosenac.services.SolicitacoesService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/solicitacao")
public class SolicitacaoController {

    @Autowired
    SolicitacoesService solicitacoesService;
    @Autowired
    SolicitacoesRepository solicitacoesRepository;

    @GetMapping
    public ModelAndView pagina(HttpSession session, Model model) {
        return solicitacoesService.pagina(session, model);
    }

    @GetMapping("/novasolicitacao")
    public ModelAndView formulario(HttpSession session, Model model) {
        return solicitacoesService.formulario(session, model);
    }

    @PostMapping("/salvar")
    public String salvar(Solicitacao solicitacao, HttpSession session) {
        return solicitacoesService.salvar(solicitacao, session);
    }

    @GetMapping("/detalhe/{id}")
    public ModelAndView detalhesSolicitacao(@PathVariable("id") int id, Model model) {
        return solicitacoesService.acessarSolicitacao(id, model);
    }

    @PostMapping("/aceitar")
    public String aceitarSolicitacao(@RequestParam("id") int id, HttpSession session, Model model) {
        return solicitacoesService.solicitacaoAprovada(id, session, model);
    }

}
