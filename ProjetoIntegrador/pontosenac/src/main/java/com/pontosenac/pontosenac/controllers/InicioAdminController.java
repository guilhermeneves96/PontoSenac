package com.pontosenac.pontosenac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import com.pontosenac.pontosenac.repository.RegistroPontoRepository;
import com.pontosenac.pontosenac.services.InicioAdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/inicio")
public class InicioAdminController {

    @Autowired
    RegistroPontoRepository registroPontoRepository;
    @Autowired
    InicioAdminService inicioAdminService;

    @GetMapping
    public ModelAndView paginaIncio(Model model, HttpSession session) {
        return inicioAdminService.paginaInicio(model, session);
    }

}
