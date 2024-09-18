package com.pontosenac.pontosenac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pontosenac.pontosenac.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

    Perfil findByPermissao(String permissao);

}
