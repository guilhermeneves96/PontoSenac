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

    public String solicitacaoAprovada(Solicitacao solicitacao, HttpSession session, Model model) {
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");

        if (pessoa == null) {
            return "redirect:/login";
        }

        // Data data = new Data();
        Hora hora = new Hora();

        String dataSolicitacao = solicitacao.getDataSolicita();
        String horaEntreda = solicitacao.getHoraEntrada();
        String horaSaida = solicitacao.getHoraSaida();

        List<RegistroPonto> registroEncontrado = registroPontoRepository.findByPessoaAndData(solicitacao.getPessoa(),
                dataSolicitacao);

        if (registroEncontrado.isEmpty()) {

            RegistroPonto novoRegistro = new RegistroPonto();

            novoRegistro.setPessoa(solicitacao.getPessoa());
            novoRegistro.setData(dataSolicitacao);
            novoRegistro.setHoraEntrada(horaEntreda);
            novoRegistro.setHoraSaida(horaSaida);
            novoRegistro.setPeriodo(hora.definirPeriodo(horaEntreda));
            registroPontoRepository.save(novoRegistro);

            solicitacao.setSolicitacaoStatus(SolicitacaoStatus.CONCLUÍDO);
            solicitacoesRepository.save(solicitacao);

        } else {
            System.out.println("Verificando registro existente...");
            boolean registroAtualizado = false;

            // Verifica se a solicitação cruza diferentes períodos
            Periodo periodoEntrada = hora.definirPeriodo(horaEntreda);
            Periodo periodoSaida = hora.definirPeriodo(horaSaida);

            // Se o horário de entrada e saída caem em períodos diferentes
            if (!periodoEntrada.equals(periodoSaida)) {
                // Se o horário de entrada está no período matutino e saída no vespertino
                if (periodoEntrada == Periodo.MATUTINO && periodoSaida == Periodo.VESPERTINO) {
                    // Cria um registro para o período matutino
                    RegistroPonto registroMatutino = new RegistroPonto();
                    registroMatutino.setPessoa(pessoa);
                    registroMatutino.setData(dataSolicitacao);
                    registroMatutino.setHoraEntrada(horaEntreda);
                    registroMatutino.setHoraSaida("12:00"); // Fim do período matutino
                    registroMatutino.setPeriodo(Periodo.MATUTINO);
                    registroPontoRepository.save(registroMatutino);

                    // Cria um registro para o período vespertino
                    RegistroPonto registroVespertino = new RegistroPonto();
                    registroVespertino.setPessoa(pessoa);
                    registroVespertino.setData(dataSolicitacao);
                    registroVespertino.setHoraEntrada("12:01"); // Início do período vespertino
                    registroVespertino.setHoraSaida(horaSaida);
                    registroVespertino.setPeriodo(Periodo.VESPERTINO);
                    registroPontoRepository.save(registroVespertino);

                    registroAtualizado = true;
                }
                // Verifica outros intervalos de períodos, como vespertino -> noturno ou
                // matutino -> noturno
                // Adapte conforme os diferentes cruzamentos de períodos que você precisa lidar
            } else {
                // Caso a solicitação esteja completamente dentro de um único período
                for (RegistroPonto registroPonto : registroEncontrado) {
                    // Verifica se o período do registro existente é diferente do período atual
                    if (registroPonto.getPeriodo() != periodoEntrada) {
                        // Cria um novo registro para o período diferente
                        RegistroPonto novoRegistro = new RegistroPonto();
                        novoRegistro.setPessoa(pessoa);
                        novoRegistro.setData(dataSolicitacao);
                        novoRegistro.setHoraEntrada(horaEntreda);
                        novoRegistro.setHoraSaida(horaSaida);
                        novoRegistro.setPeriodo(periodoEntrada);
                        registroPontoRepository.save(novoRegistro);
                        registroAtualizado = true;
                    } else if (registroPonto.getPeriodo() == periodoEntrada
                            && !registroPonto.getHoraEntrada().isEmpty()
                            && (registroPonto.getHoraSaida() == null || registroPonto.getHoraSaida().isEmpty())) {
                        // Atualiza a hora de saída
                        registroPonto.setHoraSaida(horaSaida);
                        registroPontoRepository.save(registroPonto);
                        registroAtualizado = true;
                    }
                }
            }

            // Caso nenhum registro tenha sido atualizado, você pode lançar um erro ou
            // mensagem
            if (!registroAtualizado) {
                System.out.println("Nenhum registro foi atualizado.");
            }
        }

        return "redirect:/solicitacoes";
    }

}
