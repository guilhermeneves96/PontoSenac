package com.pontosenac.pontosenac.model;

import java.io.ObjectInputFilter.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String dataAbertura, dataSolicita, decricao;

    @ManyToOne(cascade = CascadeType.ALL)
    private Pessoa pessoa;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private TipoSolicitacao tipoSolicitacao;

    public Solicitacao() {
    }

    public Solicitacao(int id, String dataAbertura, String dataSolicita, String decricao, Pessoa pessoa, Status status,
            TipoSolicitacao tipoSolicitacao) {
        this.id = id;
        this.dataAbertura = dataAbertura;
        this.dataSolicita = dataSolicita;
        this.decricao = decricao;
        this.pessoa = pessoa;
        this.status = status;
        this.tipoSolicitacao = tipoSolicitacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getDataSolicita() {
        return dataSolicita;
    }

    public void setDataSolicita(String dataSolicita) {
        this.dataSolicita = dataSolicita;
    }

    public String getDecricao() {
        return decricao;
    }

    public void setDecricao(String decricao) {
        this.decricao = decricao;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TipoSolicitacao getTipoSolicitacao() {
        return tipoSolicitacao;
    }

    public void setTipoSolicitacao(TipoSolicitacao tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }

}
