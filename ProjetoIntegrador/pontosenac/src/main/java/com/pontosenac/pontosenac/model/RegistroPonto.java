package com.pontosenac.pontosenac.model;

import com.pontosenac.pontosenac.componentes.Periodo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class RegistroPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String data;

    @Enumerated(EnumType.STRING)
    private Periodo periodo;

    private String horaEntrada, horaSaida;

    @ManyToOne(cascade = CascadeType.ALL)
    private Pessoa pessoa;

    public RegistroPonto() {
    }

    public RegistroPonto(int id, String data, Periodo periodo, String horaEntrada, String horaSaida, Pessoa pessoa) {
        this.id = id;
        this.data = data;
        this.periodo = periodo;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.pessoa = pessoa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

}
