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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.repository.RegistroPontoRepository;
import com.pontosenac.pontosenac.services.ColaboradorService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/colaboradores")
public class ColaboradoresControllers {

    @Autowired
    RegistroPontoRepository registroPontoRepository;
    @Autowired
    ColaboradorService colaboradorService;

    @GetMapping("/lista")
    public ModelAndView colaboradores() {
        return colaboradorService.listaColaboradores();
    }

    @GetMapping("/formulario")
    public ModelAndView formularioColaborador(Model model) {
        return colaboradorService.formularioColaborador(model);
    }

    @PostMapping("/salvar")
    public String salvarColaborador(Pessoa pessoa, Model model, RedirectAttributes redirectAttributes) {
        return colaboradorService.salvarColaborador(pessoa, model, redirectAttributes);
    }

    @GetMapping("/excluir/{id}")
    public String excluirColaborador(@PathVariable("id") int id) {
        return colaboradorService.excluirColaborador(id);
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") int id, Model model) {
        return colaboradorService.editarColaborador(id, model);
    }

    @GetMapping("/detalhes/{id}")
    public ModelAndView detalhesColaborador(@PathVariable("id") int id, Model model, HttpSession session) {
        return colaboradorService.paginaDetalheColaborador(id, model, session);
    }

    @PostMapping("/detalhes/{id}")
    public ModelAndView filtro(@PathVariable("id") int id, Model model, HttpSession session,
            @RequestParam("filtroMes") String filtroMes) {
        return colaboradorService.filtrarDetalheColaborador(id, filtroMes, model, session);
    }

    @PostMapping
    public ModelAndView filtro(@RequestParam("filtroMes") String filtroMes, Model model, HttpSession session) {
        return colaboradorService.filtrarRegistrosPonto(filtroMes, session, model);
    }

}
