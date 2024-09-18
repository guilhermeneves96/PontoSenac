package com.pontosenac.pontosenac.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.repository.PessoaRepository;

@Service
public class PessoasService {

    @Autowired
    PessoaRepository repository;

    public List<Pessoa> listarPessoas() {
        return repository.findAll();
    }

    public void salvar(Pessoa pessoa) {
        repository.save(pessoa);
    }

    public void excluir(int id) {
        repository.deleteById(id);
    }

}
