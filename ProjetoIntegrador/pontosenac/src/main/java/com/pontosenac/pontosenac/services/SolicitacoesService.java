package com.pontosenac.pontosenac.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.pontosenac.pontosenac.componentes.Data;
import com.pontosenac.pontosenac.componentes.Hora;
import com.pontosenac.pontosenac.componentes.Periodo;
import com.pontosenac.pontosenac.componentes.SolicitacaoStatus;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.RegistroPonto;
import com.pontosenac.pontosenac.model.Solicitacao;
import com.pontosenac.pontosenac.model.TipoSolicitacao;
import com.pontosenac.pontosenac.repository.PessoaRepository;
import com.pontosenac.pontosenac.repository.RegistroPontoRepository;
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
    @Autowired
    RegistroPontoRepository registroPontoRepository;

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

    public String solicitacaoAprovada(int id, HttpSession session, Model model) {
        Solicitacao solicitacaoOpt = solicitacoesRepository.findById(id).orElse(null);
        if (solicitacaoOpt == null) {
            return "redirect:/solicitacao?erro=solicitacao-nao-encontrada";
        }

        Hora hora = new Hora();
        Periodo periodoEntrada = hora.definirPeriodo(solicitacaoOpt.getHoraEntrada());
        Periodo periodoSaida = hora.definirPeriodo(solicitacaoOpt.getHoraSaida());

        // Define a solicitação como concluída
        solicitacaoOpt.setSolicitacaoStatus(SolicitacaoStatus.CONCLUÍDO);
        solicitacoesRepository.save(solicitacaoOpt);

        // Busca registros existentes para o dia e pessoa
        List<RegistroPonto> registrosPontos = registroPontoRepository
                .findByPessoaAndData(solicitacaoOpt.getPessoa(), solicitacaoOpt.getDataSolicita());

        if (registrosPontos.isEmpty()) {
            // Se não há registros, cria um novo ou divide por períodos
            if (periodoEntrada != periodoSaida) {
                // Caso entrada e saída sejam em períodos diferentes, divide o registro
                criarNovoRegistro(solicitacaoOpt.getPessoa(), solicitacaoOpt.getDataSolicita(),
                        solicitacaoOpt.getHoraEntrada(), "12:00:00", periodoEntrada); // Matutino termina às 12h

                criarNovoRegistro(solicitacaoOpt.getPessoa(), solicitacaoOpt.getDataSolicita(),
                        "12:01:00", solicitacaoOpt.getHoraSaida(), periodoSaida); // Vespertino começa às 12h01
            } else {
                // Caso entrada e saída sejam no mesmo período
                criarNovoRegistro(solicitacaoOpt.getPessoa(), solicitacaoOpt.getDataSolicita(),
                        solicitacaoOpt.getHoraEntrada(), solicitacaoOpt.getHoraSaida(), periodoEntrada);
            }
        } else {
            // Atualiza registros existentes ou cria novos
            boolean registroAtualizado = false;

            for (RegistroPonto registro : registrosPontos) {
                if (registro.getPeriodo() == periodoEntrada && registro.getHoraSaida() == null) {
                    // Atualiza a hora de saída para o registro do mesmo período
                    registro.setHoraSaida(solicitacaoOpt.getHoraSaida());
                    registroPontoRepository.save(registro);
                    registroAtualizado = true;
                }
            }

            if (!registroAtualizado) {
                // Cria novos registros se os períodos forem diferentes
                if (periodoEntrada != periodoSaida) {
                    criarNovoRegistro(solicitacaoOpt.getPessoa(), solicitacaoOpt.getDataSolicita(),
                            solicitacaoOpt.getHoraEntrada(), "12:00:00", periodoEntrada);

                    criarNovoRegistro(solicitacaoOpt.getPessoa(), solicitacaoOpt.getDataSolicita(),
                            "12:01:00", solicitacaoOpt.getHoraSaida(), periodoSaida);
                } else {
                    criarNovoRegistro(solicitacaoOpt.getPessoa(), solicitacaoOpt.getDataSolicita(),
                            solicitacaoOpt.getHoraEntrada(), solicitacaoOpt.getHoraSaida(), periodoEntrada);
                }
            }
        }

        return "redirect:/solicitacao";
    }

    // Método auxiliar para criar novo registro
    private void criarNovoRegistro(Pessoa pessoa, String data, String horaEntrada, String horaSaida, Periodo periodo) {
        RegistroPonto novoRegistro = new RegistroPonto();
        novoRegistro.setPessoa(pessoa);
        novoRegistro.setData(data);
        novoRegistro.setHoraEntrada(horaEntrada);
        novoRegistro.setHoraSaida(horaSaida);
        novoRegistro.setPeriodo(periodo);
        registroPontoRepository.save(novoRegistro);
    }

}
