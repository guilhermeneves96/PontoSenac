package com.pontosenac.pontosenac.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.RegistroPonto;
import com.pontosenac.pontosenac.repository.RegistroPontoRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class RelatorioService {

    @Autowired
    RegistroPontoRepository registroPontoRepository;

    public ModelAndView listarRegistrosPonto(Model model, HttpSession session) {
        ModelAndView mv = new ModelAndView("relatorio");
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        List<RegistroPonto> registrosPonto = registroPontoRepository.findByPessoaId(pessoa.getId());
        mv.addObject("registrosPonto", registrosPonto);

        Set<String> mesesEAnosUnicos = lsitarMesAno(registrosPonto);
        mv.addObject("mesAno", mesesEAnosUnicos);

        return mv;
    }

    public Set<String> lsitarMesAno(List<RegistroPonto> registros) {

        Set<String> mesesEAnos = new HashSet<>();
        DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd, MMM yyyy")
                .withLocale(Locale.forLanguageTag("pt-BR"));

        for (RegistroPonto registro : registros) {
            try {
                // Converte a string de data para LocalDate
                LocalDate data = LocalDate.parse(registro.getData(), formatar);
                // Cria uma string no formato "MMM yyyy"
                String mesAno = data.format(DateTimeFormatter.ofPattern("MMM yyyy"));
                mesesEAnos.add(mesAno);
            } catch (Exception e) {
                // Tratar exceção se a string não estiver no formato esperado
                e.printStackTrace();
            }
        }

        return mesesEAnos;
    }

    public ModelAndView filtrarRegistrosPonto(String mesAno, HttpSession session, Model model) {

        ModelAndView mv = new ModelAndView("relatorio");
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        List<RegistroPonto> registrosPonto = registroPontoRepository.findByPessoaId(pessoa.getId());
        List<RegistroPonto> registroFiltrado = registroPontoRepository.findByDataEndsWith(mesAno);
        boolean limpar = true;
        mv.addObject("registrosPonto", registroFiltrado);
        mv.addObject("mesAnoSelecionado", mesAno); // Adicione esta linha
        Set<String> mesesEAnosUnicos = lsitarMesAno(registrosPonto);
        mv.addObject("mesAno", mesesEAnosUnicos);
        model.addAttribute("limpar", limpar);
        return mv;

    }

}
