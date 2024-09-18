package com.pontosenac.pontosenac.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Funcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String descricao;

    @OneToMany(mappedBy = "funcao")
    List<Pessoa> pessoas;

    public Funcao(int id, String descricao, List<Pessoa> pessoas) {
        this.id = id;
        this.descricao = descricao;
        this.pessoas = pessoas;
    }

    public Funcao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
