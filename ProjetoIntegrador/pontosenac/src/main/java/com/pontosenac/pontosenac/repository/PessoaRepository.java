package com.pontosenac.pontosenac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pontosenac.pontosenac.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    Optional<Pessoa> findByMatricula(String matricula);

    Optional<Pessoa> findByCpf(String cpf);

}

/*
 * Optional<Pessoa>: O uso de Optional ajuda a lidar com a possibilidade de não
 * encontrar uma pessoa com o CPF ou matrícula fornecidos, evitando
 * NullPointerExceptions.
 */