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

        if (hora.matches("\\d{2}:\\d{2}")) {
            hora += ":00"; // Adiciona ":00" ao final para o formato HH:mm:ss
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime horaAtual;
        try {
            horaAtual = LocalTime.parse(hora, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de hora inválido: " + hora, e);
        }

        // Definição dos intervalos
        LocalTime inicioMatutino = LocalTime.of(6, 0, 0);
        LocalTime fimMatutino = LocalTime.of(12, 0, 0);
        LocalTime inicioVespertino = LocalTime.of(12, 0, 1);
        LocalTime fimVespertino = LocalTime.of(18, 0, 0);
        LocalTime inicioNoturno = LocalTime.of(18, 0, 1);
        LocalTime fimNoturno = LocalTime.of(23, 59, 59);

        if (!horaAtual.isBefore(inicioMatutino) && !horaAtual.isAfter(fimMatutino)) {
            return Periodo.MATUTINO;
        } else if (!horaAtual.isBefore(inicioVespertino) && !horaAtual.isAfter(fimVespertino)) {
            return Periodo.VESPERTINO;
        } else if (!horaAtual.isBefore(inicioNoturno) && !horaAtual.isAfter(fimNoturno)) {
            return Periodo.NOTURNO;
        } else {
            throw new IllegalArgumentException("Hora fora dos intervalos definidos: " + hora);
        }
    }

}