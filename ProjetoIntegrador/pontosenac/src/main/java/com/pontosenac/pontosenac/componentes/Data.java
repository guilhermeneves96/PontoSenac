package com.pontosenac.pontosenac.componentes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Data {

    // Obtem a data Atual
    private LocalDate dataAtual = LocalDate.now();
    // Defini o formato desejado
    private DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd, MMM yyyy")
            .withLocale(Locale.forLanguageTag("pt-BR"));

    // private DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd, MMM
    // yyyy")
    // .withLocale(new Locale("pt", "BR")); // Use o construtor Locale

    // Formata a data
    private String dataFormatada = dataAtual.format(formatar);

    public String dataAtual() {
        return dataFormatada;
    }

}
