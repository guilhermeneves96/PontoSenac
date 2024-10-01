package com.pontosenac.pontosenac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pontosenac.pontosenac.model.TipoSolicitacao;

@Repository
public interface TipoSolicitacaoRepository extends JpaRepository<TipoSolicitacao, Integer> {

}
