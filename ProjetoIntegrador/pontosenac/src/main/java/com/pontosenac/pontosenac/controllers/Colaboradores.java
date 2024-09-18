package com.pontosenac.pontosenac.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pontosenac.pontosenac.model.Funcao;
import com.pontosenac.pontosenac.model.Perfil;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.repository.FuncaoRepository;
import com.pontosenac.pontosenac.repository.PerfilRepository;
import com.pontosenac.pontosenac.repository.PessoaRepository;
import com.pontosenac.pontosenac.services.PessoasService;

@Controller
@RequestMapping("/colaboradores")
public class Colaboradores {

    @Autowired
    private PessoasService pessoasService;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private FuncaoRepository funcaoRepository;
    @Autowired
    private PerfilRepository perfilRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/lista")
    public ModelAndView colaboradores() {
        ModelAndView mv = new ModelAndView("colaboradores");
        List<Pessoa> colaboradores = pessoasService.listarPessoas();
        mv.addObject("colaboradores", colaboradores);
        return mv;
    }

    @GetMapping("/formulario")
    public ModelAndView formularioColaborador(Model model) {
        ModelAndView mv = new ModelAndView("formsColaborador");
        List<Funcao> funcoes = funcaoRepository.findAll();
        mv.addObject("funcao", funcoes);

        List<Perfil> perfil = perfilRepository.findAll();
        mv.addObject("perfil", perfil);

        model.addAttribute("titulo", "Novo funcionario");
        return mv;
    }

    @PostMapping("/salvar")
    public String salvarColaborador(Pessoa pessoa, Model model, RedirectAttributes redirectAttributes) {

        String url;

        if (pessoa.getId() > 0) {
            pessoa.setSenha(passwordEncoder.encode(pessoa.getSenha()));
            pessoa.setCpf(pessoa.getCpf() + "");
            pessoasService.salvar(pessoa);
            url = "redirect:/colaboradores/lista"; // Redirecionamento para a lista
        } else {
            Optional<Pessoa> cpfEncontrado = pessoaRepository.findByCpf(pessoa.getCpf());
            Optional<Pessoa> matriculaEncontrada = pessoaRepository.findByMatricula(pessoa.getMatricula());

            if (cpfEncontrado.isPresent() || matriculaEncontrada.isPresent()) {
                if (cpfEncontrado.isPresent()) {
                    redirectAttributes.addFlashAttribute("erroCpf", "CPF já cadastrado");
                }
                if (matriculaEncontrada.isPresent()) {
                    redirectAttributes.addFlashAttribute("erroMat", "Matrícula já cadastrada");
                }

                url = "redirect:/colaboradores/formulario"; // Redirecionamento ao formulário
            } else {

                pessoa.setSenha(passwordEncoder.encode(pessoa.getSenha()));
                pessoasService.salvar(pessoa);
                url = "redirect:/colaboradores/lista"; // Redirecionamento para a lista
            }
        }

        return url; // Isso agora sempre retorna uma URL de redirecionamento

    }

    @GetMapping("/excluir/{id}")
    public String excluirColaborador(@PathVariable("id") int id) {
        pessoasService.excluir(id);
        return "redirect:/colaboradores/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") int id, Model model) {

        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);

        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            model.addAttribute("colaborador", pessoa);
            model.addAttribute("funcao", funcaoRepository.findAll());
            model.addAttribute("perfil", perfilRepository.findAll());
            model.addAttribute("titulo", "Editar Colaborador");
            return "formsColaborador";
        } else {

            return "redirect:/colaboradores/lista";
        }
    }

}
