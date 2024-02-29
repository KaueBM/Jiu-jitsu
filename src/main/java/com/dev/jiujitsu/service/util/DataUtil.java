package com.dev.jiujitsu.service.util;

import com.dev.jiujitsu.service.FeriadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class DataUtil {

    @Autowired
    private FeriadoService feriadoService;

    public boolean diaAdicionadoEhMenor(int numeroDias, int diasAdicionados) {
        return diasAdicionados < numeroDias;
    }

    public boolean ehFinalDeSemana(LocalDate data) {
        DayOfWeek dayOfWeek = data.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public LocalDate pulaSemanaAposLimite(LocalDate data) {
        return data.plusDays(7 - data.getDayOfWeek().getValue());
    }

    public String retornaDiaDaSemana(DayOfWeek data) {
        return switch (data) {
            case SUNDAY -> "Domingo";
            case MONDAY -> "Segunda";
            case TUESDAY -> "Terça";
            case WEDNESDAY -> "Quarta";
            case THURSDAY -> "Quinta";
            case FRIDAY -> "Sexta";
            case SATURDAY -> "Sábado";
        };
    }

    public LocalDate pulaFinaisDeSemanaFeriados(LocalDate data) {
        if(ehFinalDeSemana(data) || feriadoService.ehFeriado(data)){
            data = pularParaProximoDiaUtil(data);
        }
        return data;
    }

    private LocalDate pularParaProximoDiaUtil(LocalDate data) {
        data = switch (data.getDayOfWeek()) {
            case FRIDAY -> data.plusDays(3);
            case SATURDAY -> data.plusDays(2);
            default -> data.plusDays(1);
        };

        return pulaFinaisDeSemanaFeriados(data);
    }
}
