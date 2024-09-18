package com.pontosenac.pontosenac.componentes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Data {

    // Obtem a data Atual
    private LocalDate dataAtual = LocalDate.now();
    // Defini o formato desejado
    private DateTimeFormatter formatar = DateTimeFormatter.ofPattern("d, MMM yyy").withLocale(new Locale("pt", "BR"));
    // Formata a data
    private String dataFormatada = dataAtual.format(formatar);

    public String dataAtual() {
        return dataFormatada;
    }

}
