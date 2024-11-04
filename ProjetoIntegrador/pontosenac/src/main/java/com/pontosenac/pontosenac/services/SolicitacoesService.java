package com.pontosenac.pontosenac.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.pontosenac.pontosenac.componentes.Data;
import com.pontosenac.pontosenac.componentes.SolicitacaoStatus;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.Solicitacao;
import com.pontosenac.pontosenac.model.TipoSolicitacao;
import com.pontosenac.pontosenac.repository.PessoaRepository;
import com.pontosenac.pontosenac.repository.SolicitacoesRepository;
import com.pontosenac.pontosenac.repository.TipoSolicitacaoRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class SolicitacoesService {

    @Autowired
    TipoSolicitacaoRepository tipoSolicitacaoRepository;
    @Autowired
    SolicitacoesRepository solicitacoesRepository;
    @Autowired
    PessoaRepository pessoaRepository;

    public ModelAndView pagina(HttpSession session, Model model) {
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        return pessoa.getPerfil().getPermissao().equalsIgnoreCase("admin") ? paginaAdm(session, model)
                : paginaUser(session, model);
    }

    public ModelAndView paginaUser(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("listaSolicitacao");
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        if (pessoa != null) {
            List<Solicitacao> solicitacaos = pessoa.getPerfil().getPermissao().equalsIgnoreCase("admin")
                    ? solicitacoesRepository.findAll()
                    : solicitacoesRepository.findByPessoa(pessoa);
            mv.addObject("solicitacoes", solicitacaos);
        } else {
            model.addAttribute("erro", "Você não esta Autenticado");
            mv.setViewName("redirect:/login");
        }

        return mv;
    }

    public ModelAndView paginaAdm(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("listaSolicitacaoAdm");
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");

        if (pessoa != null && pessoa.getPerfil().getPermissao().equalsIgnoreCase("admin")) {

            List<Solicitacao> solicitacaos = pessoa.getPerfil().getPermissao().equalsIgnoreCase("admin")
                    ? solicitacoesRepository.findAll()
                    : solicitacoesRepository.findByPessoa(pessoa);
            mv.addObject("solicitacoes", solicitacaos);

        } else {
            model.addAttribute("erro", "Você não esta Autenticado");
            mv.setViewName("redirect:/login");
        }

        return mv;
    }

    public ModelAndView formulario(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("novasolicitacao");

        List<TipoSolicitacao> TipoSolicitacao = tipoSolicitacaoRepository.findAll();
        mv.addObject("tipoSolicitacao", TipoSolicitacao);

        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        if (pessoa == null) {
            model.addAttribute("erro", "Você não esta Autenticado");
            return mv;
        }
        model.addAttribute("titulo", "Nova Solicitação");
        return mv;
    }

    public String salvar(Solicitacao solicitacao, HttpSession session) {
        Data data = new Data();
        String dataHoje = data.dataAtual();

        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");

        if (pessoa == null) {
            return "redirect:/login"; // ou qualquer página de erro apropriada
        }
        // Recupere a Pessoa do repositório para garantir que está gerenciada
        pessoa = pessoaRepository.findById(pessoa.getId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        solicitacao.setDataAbertura(dataHoje);
        solicitacao.setPessoa(pessoa);
        solicitacao.setDataSolicita(Data.converterData(solicitacao.getDataSolicita()));
        solicitacao.setSolicitacaoStatus(SolicitacaoStatus.PENDENTE);
        solicitacoesRepository.save(solicitacao);
        return "redirect:/solicitacao";
    }

    public ModelAndView acessarSolicitacao(int id, Model model) {
        ModelAndView mv = new ModelAndView("detalheSolicitacao");
        Optional<Solicitacao> solicitacaoOpt = solicitacoesRepository.findById(id);

        if (solicitacaoOpt.isPresent()) {
            mv.addObject("solicitacao", solicitacaoOpt.get()); // Extraindo o objeto
        } else {
            // Tratar o caso em que a solicitação não é encontrada
            mv.setViewName("erro"); // Redirecionar para uma página de erro, por exemplo
        }

        return mv;
    }

}
