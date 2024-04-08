package com.dev.jiujitsu.service.util;

import com.dev.jiujitsu.service.FeriadoService;
import com.dev.jiujitsu.service.RecessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class DataUtil {

    @Autowired
    private FeriadoService feriadoService;

    @Autowired
    private RecessoService recessoService;

    public boolean diaAdicionadoEhMenor(int numeroDias, int diasAdicionados) {
        return diasAdicionados < numeroDias;
    }

    public LocalDate adicionarDiasUteis(LocalDate data, int numeroDias, int diasUteisPorSemana) {
        int diasAdicionados = 0;
        int diaMesmaSemana = 0;

        while (diasAdicionados < numeroDias) {
            if (ehFinalDeSemana(data)) {
                diaMesmaSemana = 0;
            }
            data = pulaFinaisDeSemanaFeriadosRecessos(data);
            diasAdicionados++;
            diaMesmaSemana++;

            if (diaMesmaSemana > diasUteisPorSemana && diaAdicionadoEhMenor(numeroDias, diasAdicionados)) {
                data = pulaSemanaAposLimite(data);
                diaMesmaSemana = 0;
            } else if (diaAdicionadoEhMenor(numeroDias, diasAdicionados)) {
                data = data.plusDays(1);
            }
        }

        return data;
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

    public LocalDate pulaFinaisDeSemanaFeriadosRecessos(LocalDate data) {
        if(ehFinalDeSemana(data) || feriadoService.ehFeriado(data) || recessoService.ehRecesso(data)){
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

        return pulaFinaisDeSemanaFeriadosRecessos(data);
    }
}
