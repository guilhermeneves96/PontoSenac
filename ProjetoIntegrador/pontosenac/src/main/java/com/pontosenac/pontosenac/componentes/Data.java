package com.pontosenac.pontosenac.componentes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static String converterData(String data) {
        // Definindo o formato de entrada
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Definindo o formato de saída
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd, MMM yyyy", Locale.forLanguageTag("pt-BR"));

        try {
            // Convertendo a string de entrada para uma data
            java.util.Date utilDate = inputFormat.parse(data);
            // Convertendo para java.sql.Date, se necessário
            // Formatando a data no novo formato
            return outputFormat.format(utilDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // ou lançar uma exceção personalizada
        }
    }

}
