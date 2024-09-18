package com.pontosenac.pontosenac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pontosenac.pontosenac.model.Funcao;

public interface FuncaoRepository extends JpaRepository<Funcao, Integer> {

    Funcao findByDescricao(String descricao);

}
