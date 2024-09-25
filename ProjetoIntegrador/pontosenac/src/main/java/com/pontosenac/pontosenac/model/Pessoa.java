package com.pontosenac.pontosenac.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String cpf;
    private String matricula;
    private String email;
    private String senha;
    private String contato;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Perfil perfil;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Funcao funcao;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    List<RegistroPonto> registrosPonto;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    List<Solicitacao> solicitacoes;

    public Pessoa() {
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Pessoa(int id, String nome, String cpf, String matricula, String funcao, String email, String senha,
            String contato) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
        this.email = email;
        this.senha = senha;
        this.contato = contato;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<RegistroPonto> getRegistrosPonto() {
        return registrosPonto;
    }

    public void setRegistrosPonto(List<RegistroPonto> registrosPonto) {
        this.registrosPonto = registrosPonto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public List<Solicitacao> getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(List<Solicitacao> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

}
