package com.pontosenac.pontosenac.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.pontosenac.pontosenac.componentes.Data;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.RegistroPonto;
import com.pontosenac.pontosenac.repository.RegistroPontoRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class InicioAdminService {

    @Autowired
    RegistroPontoRepository registroPontoRepository;

    public ModelAndView paginaInicio(Model model, HttpSession session) {
        ModelAndView mv = new ModelAndView("inicioAdmin");
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        Data data = new Data();
        String dataHoje = data.dataAtual();

        if (pessoa != null) {
            model.addAttribute("pessoa", pessoa);
            model.addAttribute("dataAtual", dataHoje);

            // Buscar todos os registros do dia
            List<RegistroPonto> registrosPonto = registroPontoRepository.findByData(dataHoje)
                    .orElse(new ArrayList<>());

            // Filtrar registros que têm apenas horaEntrada
            List<RegistroPonto> registrosFiltrados = registrosPonto.stream()
                    .filter(registro -> registro.getHoraSaida() == null || registro.getHoraSaida().isEmpty())
                    .collect(Collectors.toList());

            // Adicionar a lista filtrada ao ModelAndView
            mv.addObject("registrosPontos", registrosFiltrados);
        } else {
            model.addAttribute("erro", "Você não está autenticado");
            mv.setViewName("redirect:/login");
        }

        return mv;
    }

}
