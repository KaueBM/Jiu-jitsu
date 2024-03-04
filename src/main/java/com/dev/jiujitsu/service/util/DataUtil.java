package com.dev.jiujitsu.service.util;

import com.dev.jiujitsu.domain.dto.DiasLivres;
import com.dev.jiujitsu.domain.entity.Feriado;
import com.dev.jiujitsu.domain.entity.Recesso;
import com.dev.jiujitsu.repository.FeriadoRepository;
import com.dev.jiujitsu.repository.RecessoRepository;
import com.dev.jiujitsu.service.FeriadoService;
import com.dev.jiujitsu.service.RecessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Component
public class DataUtil {

    @Autowired
    private FeriadoService feriadoService;

    @Autowired
    private RecessoService recessoService;

    @Autowired
    private FeriadoRepository feriadoRepository;

    @Autowired
    private RecessoRepository recessoRepository;

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

    public LocalDate pulaFinaisDeSemanaFeriadosRecessos(LocalDate data) {
        if (ehFinalDeSemana(data) || feriadoService.ehFeriado(data) || recessoService.ehRecesso(data)) {
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


    public DiasLivres buscaMaximosDeDiasLivres(int ano) {
        LocalDate inicioAno = LocalDate.of(ano, 1, 1);
        LocalDate fimAno = LocalDate.of(ano, 12, 31);

        List<Feriado> feriados = feriadoRepository.buscarFeriadoPorAno(inicioAno, fimAno);
        List<Recesso> recessos = recessoRepository.buscarRecessoPorAno(inicioAno, fimAno);
        List<LocalDate> listaDeDatasLivres = obtemListaDeDatas(feriados, recessos);

        int maximoDeDiasLivresConsecutivos = 0;
        int numAtualDeDiasLivresConsecutivos = 0;
        LocalDate diaInicial = null;
        LocalDate diaLivreInicial = null;
        LocalDate diaAtual = inicioAno;
        LocalDate diaFinal = inicioAno;
        LocalDate proximoDiaUtil;

        while (diaAtual.isBefore(fimAno.plusDays(1))) {
            if (ehDiaLivre(diaAtual, listaDeDatasLivres)) {
                if (isNull(diaInicial)) {
                    diaInicial = diaAtual;
                }

                numAtualDeDiasLivresConsecutivos++;
            } else {
                if (numAtualDeDiasLivresConsecutivos > maximoDeDiasLivresConsecutivos) {
                    diaLivreInicial = diaInicial;
                    diaFinal = diaLivreInicial.plusDays(numAtualDeDiasLivresConsecutivos - 1);
                    maximoDeDiasLivresConsecutivos = numAtualDeDiasLivresConsecutivos;
                }
                numAtualDeDiasLivresConsecutivos = 0;
                diaInicial = null;
            }

            diaAtual = diaAtual.plusDays(1);
        }
        proximoDiaUtil = pularParaProximoDiaUtil(diaFinal);

        return new DiasLivres(diaLivreInicial, diaFinal, maximoDeDiasLivresConsecutivos, proximoDiaUtil);
    }

    private boolean ehDiaLivre(LocalDate diaAtual, List<LocalDate> listaDeDatasLivres) {
        return listaDeDatasLivres.contains(diaAtual) || ehFinalDeSemana(diaAtual);
    }

    private List<LocalDate> obtemListaDeDatas(List<Feriado> feriados, List<Recesso> recessos) {
        List<LocalDate> datasFeriados = feriados.stream().map(Feriado::getData).toList();
        List<LocalDate> datasRecessos = recessos.stream().map(Recesso::getData).toList();

        return Stream.concat(datasFeriados.stream(), datasRecessos.stream())
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

}