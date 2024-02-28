package com.dev.jiujitsu.service;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.dev.jiujitsu.domain.dto.Graduacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class GraduacaoService {

    @Autowired
    private FeriadoService feriadoService;

    public Graduacao buscarDatasGraduacoes(String faixa, int aulasPorSemana, int aulasFeitas, int grausRecebidos){
        LocalDate dataAtual = LocalDate.now();
        FaixasEnum faixaAtual = FaixasEnum.valueOf(faixa);
        int aulasGraduacao = faixaAtual.getAulasRestantesParaProximaFaixa(grausRecebidos,aulasFeitas);
        LocalDate dataGraduacao = this.adicionarDiasUteis(dataAtual,aulasGraduacao, aulasPorSemana);
        Graduacao resposta = new Graduacao();
        resposta.setGraduacao(dataGraduacao);
        resposta.setAulasFaltantes(aulasGraduacao);
        this.preencherDatas(resposta, faixaAtual, dataAtual, grausRecebidos, aulasFeitas, aulasPorSemana);

        return resposta;

    }

    private void preencherDatas(Graduacao graduacao, FaixasEnum faixa, LocalDate dataAtual, int grauAtual, int aulasFeitas, int aulasPorSemana) {
        if (FaixasEnum.ehPreta(faixa)) {
            graduacao.setGraduacaoCoral(adicionarDiasUteis(dataAtual, faixa.anosParaCoral(grauAtual) * 360, 7));
            return;
        } else if(FaixasEnum.ehFaixaFinal(faixa)){
            return;
        }


        switch (grauAtual) {
            case 0:
                graduacao.setGrau1(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() -aulasFeitas, aulasPorSemana));
                graduacao.setGrau2(adicionarDiasUteis(graduacao.getGrau1(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGrau3(adicionarDiasUteis(graduacao.getGrau2(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGrau4(adicionarDiasUteis(graduacao.getGrau3(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacao(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoPreta(adicionarDiasUteis(graduacao.getGrau4(), faixa.getTotalAulasFaltantesPreta(grauAtual,aulasFeitas), aulasPorSemana));
                graduacao.setGraduacaoCoral(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                break;
            case 1:
                graduacao.setGrau2(adicionarDiasUteis(graduacao.getGrau1(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGrau3(adicionarDiasUteis(graduacao.getGrau2(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGrau4(adicionarDiasUteis(graduacao.getGrau3(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacao(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoPreta(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoCoral(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                break;
            case 2:
                graduacao.setGrau3(adicionarDiasUteis(graduacao.getGrau2(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGrau4(adicionarDiasUteis(graduacao.getGrau3(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacao(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoPreta(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoCoral(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                break;
            case 3:
                graduacao.setGrau4(adicionarDiasUteis(graduacao.getGrau3(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacao(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoPreta(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoCoral(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                break;
            case 4:
                graduacao.setGraduacao(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoPreta(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoCoral(graduacao.getGraduacaoPreta().plusYears(faixa.anosParaCoral(0)));
                break;
        }

    }

    private LocalDate adicionarDiasUteis(LocalDate data, int numeroDias, int diasUteisPorSemana) {
        int diasAdicionados = 0;

        while (diasAdicionados < numeroDias) {
            data = data.plusDays(1);

            if (ehDiaUtil(data) && !feriadoService.ehFeriado(data)) {
                diasAdicionados++;
            }

            if (diasAdicionados % diasUteisPorSemana == 0) {
                data = pularSemana(data);
            }
        }

        return data;
    }

    private boolean ehDiaUtil(LocalDate data) {
        DayOfWeek dayOfWeek = data.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    private LocalDate pularSemana(LocalDate data) {
        return switch (data.getDayOfWeek()) {
            case SUNDAY -> data.plusDays(7);
            case MONDAY -> data.plusDays(6);
            case TUESDAY -> data.plusDays(5);
            case WEDNESDAY -> data.plusDays(4);
            case THURSDAY -> data.plusDays(3);
            case FRIDAY -> data.plusDays(2);
            case SATURDAY -> data.plusDays(1);
        };
    }

}
