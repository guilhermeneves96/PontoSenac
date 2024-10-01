package com.pontosenac.pontosenac.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class TipoSolicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String descricao;

    @OneToMany(mappedBy = "tipoSolicitacao")
    List<Solicitacao> solicitacao;

    public TipoSolicitacao() {
    }

    public TipoSolicitacao(int id, String descricao, List<Solicitacao> solicitacao) {
        this.id = id;
        this.descricao = descricao;
        this.solicitacao = solicitacao;
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

    public List<Solicitacao> getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(List<Solicitacao> solicitacao) {
        this.solicitacao = solicitacao;
    }

}
