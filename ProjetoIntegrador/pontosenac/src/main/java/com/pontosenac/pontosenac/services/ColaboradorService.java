package com.pontosenac.pontosenac.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pontosenac.pontosenac.model.Funcao;
import com.pontosenac.pontosenac.model.Perfil;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.RegistroPonto;
import com.pontosenac.pontosenac.repository.FuncaoRepository;
import com.pontosenac.pontosenac.repository.PerfilRepository;
import com.pontosenac.pontosenac.repository.PessoaRepository;
import com.pontosenac.pontosenac.repository.RegistroPontoRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ColaboradorService {

    @Autowired
    PessoaRepository pessoaRepository;
    @Autowired
    PessoasService pessoasService;
    @Autowired
    RegistroPontoRepository registroPontoRepository;
    @Autowired
    FuncaoRepository funcaoRepository;
    @Autowired
    PerfilRepository perfilRepository;
    @Autowired
    RelatorioService relatorioService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ModelAndView listaColaboradores() {
        ModelAndView mv = new ModelAndView("colaboradores");
        List<Pessoa> colaboradores = pessoasService.listarPessoas();
        mv.addObject("colaboradores", colaboradores);
        return mv;
    }

    public ModelAndView formularioColaborador(Model model) {
        ModelAndView mv = new ModelAndView("formsColaborador");
        List<Funcao> funcoes = funcaoRepository.findAll();
        mv.addObject("funcao", funcoes);

        List<Perfil> perfil = perfilRepository.findAll();
        mv.addObject("perfil", perfil);

        model.addAttribute("titulo", "Novo funcionario");
        return mv;
    }

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

        return url;
    }

    public String excluirColaborador(int id) {
        pessoasService.excluir(id);
        return "redirect:/colaboradores/lista";
    }

    public String editarColaborador(int id, Model model) {
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

    public ModelAndView paginaDetalheColaborador(int id, Model model, HttpSession session) {
        ModelAndView mv = new ModelAndView("detalheColaborador");
        Optional<Pessoa> optionalPessoa = pessoaRepository.findById(id);

        Pessoa pessoaLogada = (Pessoa) session.getAttribute("pessoaAutenticada");
        if (pessoaLogada == null) {
            // Lógica para lidar com o caso em que a pessoa não está autenticada
            mv.setViewName("redirect:/login"); // ou qualquer outra lógica
            return mv;
        }

        if (optionalPessoa.isPresent()) {
            Pessoa pessoa = optionalPessoa.get();
            mv.addObject("pessoa", pessoa); // Adiciona a Pessoa diretamente ao modelo

            List<RegistroPonto> registrosPonto = registroPontoRepository.findByPessoaId(pessoa.getId());
            mv.addObject("registrosPontos", registrosPonto);

            Set<String> mesesEAnosUnicos = relatorioService.listarMesAno(registrosPonto);
            mv.addObject("mesAno", mesesEAnosUnicos);

        } else {
            // Trate o caso em que a pessoa não é encontrada, por exemplo:
            mv.addObject("error", "Colaborador não encontrado");
        }

        return mv;
    }

    public ModelAndView filtrarRegistrosPonto(String mesAno, HttpSession session, Model model) {
        return relatorioService.filtrarRegistrosPonto("detalheColaborador", mesAno, session, model);
    }

    public ModelAndView filtrarDetalheColaborador(int id, String mesAno, Model model, HttpSession session) {
        ModelAndView mv = new ModelAndView("detalheColaborador");
        Optional<Pessoa> optionalPessoa = pessoaRepository.findById(id);

        Pessoa pessoaLogada = (Pessoa) session.getAttribute("pessoaAutenticada");
        if (pessoaLogada == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        if (optionalPessoa.isPresent()) {
            Pessoa pessoa = optionalPessoa.get();
            mv.addObject("pessoa", pessoa); // Adiciona a Pessoa ao modelo

            List<RegistroPonto> registrosPonto = registroPontoRepository.findByPessoaId(pessoa.getId());
            List<RegistroPonto> registroFiltrado = registroPontoRepository.findByPessoaAndData(pessoa, mesAno);
            // List<RegistroPonto> registroFiltrado =
            // registroPontoRepository.findByDataEndsWith(mesAno);

            System.out.println(registroFiltrado.size());

            boolean limpar = true;
            mv.addObject("registrosPontos", registroFiltrado); // Corrigir nome da variável
            mv.addObject("mesAnoSelecionado", mesAno);
            Set<String> mesesEAnosUnicos = relatorioService.listarMesAno(registrosPonto);
            mv.addObject("mesAno", mesesEAnosUnicos);
            model.addAttribute("limpar", limpar);
        } else {
            mv.addObject("error", "Colaborador não encontrado");
        }

        return mv;
    }

}
