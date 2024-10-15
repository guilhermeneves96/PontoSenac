package com.pontosenac.pontosenac.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.Solicitacao;

@Repository
public interface SolicitacoesRepository extends JpaRepository<Solicitacao, Integer> {

    List<Solicitacao> findByPessoa(Pessoa pessoa);

}
