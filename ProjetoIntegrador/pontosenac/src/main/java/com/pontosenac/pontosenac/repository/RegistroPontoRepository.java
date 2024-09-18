package com.pontosenac.pontosenac.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.RegistroPonto;

@Repository
public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Integer> {

    List<RegistroPonto> findByPessoaId(int id);

    Optional<List<RegistroPonto>> findByData(String data);

    List<RegistroPonto> findByPessoaAndData(Pessoa pessoa, String data);

}
