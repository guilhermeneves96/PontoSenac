package com.pontosenac.pontosenac.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.RegistroPonto;

@Repository
public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Integer> {

    List<RegistroPonto> findByPessoaId(int id);

    Optional<List<RegistroPonto>> findByData(String data);

    List<RegistroPonto> findByDataEndsWith(String mesANo);

    List<RegistroPonto> findByPessoaAndDataEndsWith(Pessoa pessoam, String mesANo);

    List<RegistroPonto> findByPessoaAndData(Pessoa pessoa, String data);

    @Query("SELECT rp FROM RegistroPonto rp WHERE rp.pessoa = :pessoa AND rp.data LIKE CONCAT('%', :mesAno)")
    List<RegistroPonto> findByPessoaAndMesAno(@Param("pessoa") Pessoa pessoa, @Param("mesAno") String mesAno);

    // List<RegistroPonto> findByPessoaIdAndDataMesAno(int pessoaId, String mesAno);

}
