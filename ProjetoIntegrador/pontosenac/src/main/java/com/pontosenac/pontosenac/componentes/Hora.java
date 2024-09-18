package com.pontosenac.pontosenac.componentes;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Hora {

    // Obtem a hora atual;
    private LocalTime horaAgora = LocalTime.now();
    // Defini o formato
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
    // Formata a hora atual;
    private String horaFormatada = horaAgora.format(format);

    public String horaAtual() {
        return horaFormatada;
    }

    public Periodo definirPeriodo(String hora) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime horaAtual;
        try {
            horaAtual = LocalTime.parse(hora, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de hora inválido: " + hora, e);
        }

        LocalTime inicioMatutino = LocalTime.of(6, 0, 0);
        LocalTime fimMatutino = LocalTime.of(12, 0, 0);
        LocalTime inicioVespertino = LocalTime.of(12, 1, 0);
        LocalTime fimVespertino = LocalTime.of(18, 0, 0);
        LocalTime inicioNoturno = LocalTime.of(18, 1, 0);
        LocalTime fimNoturno = LocalTime.of(23, 59, 59);

        if (horaAtual.isAfter(inicioMatutino.minusSeconds(1)) && horaAtual.isBefore(fimMatutino)) {
            return Periodo.MATUTINO;
        } else if (horaAtual.isAfter(inicioVespertino.minusSeconds(1)) && horaAtual.isBefore(fimVespertino)) {
            return Periodo.VESPERTINO;
        } else if (horaAtual.isAfter(inicioNoturno.minusSeconds(1)) && horaAtual.isBefore(fimNoturno)) {
            return Periodo.NOTURNO;
        } else {
            throw new IllegalArgumentException("Hora fora dos intervalos definidos: " + hora);
        }

    }
}