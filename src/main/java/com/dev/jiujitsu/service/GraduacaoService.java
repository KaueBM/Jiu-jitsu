package com.dev.jiujitsu.service;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.dev.jiujitsu.domain.dto.Graduacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GraduacaoService {

    @Autowired
    private FeriadoService feriadoService;

    public List<Graduacao> buscarDatasGraduacoes(String faixa, int aulasPorSemana, int aulasFeitas, int grausRecebidos){
        LocalDate dataAtual = LocalDate.now();
        FaixasEnum faixaAtual = FaixasEnum.obterFaixaPorNome(faixa);
        int aulasGraduacao = faixaAtual.getAulasRestantesParaProximaFaixa(grausRecebidos,aulasFeitas);
        List<FaixasEnum> faixasRestantes = faixaAtual.buscaListaFaixasQueFaltam(faixa);
        LocalDate dataGraduacao = this.adicionarDiasUteis(dataAtual,aulasGraduacao, aulasPorSemana);
        List<Graduacao> graduacoes = new ArrayList<>();
        Graduacao graduacaoAtual = new Graduacao();
        graduacaoAtual.setFaixa(faixaAtual);
        graduacaoAtual.setGraduacao(dataGraduacao);
        graduacaoAtual.setAulasProxFaixa(aulasGraduacao);
        graduacaoAtual.setAulasParaPreta(faixaAtual.getTotalAulasFaltantesPreta(grausRecebidos,aulasFeitas));
        this.preencherDatas(graduacaoAtual, faixaAtual, dataAtual, grausRecebidos, aulasFeitas, aulasPorSemana);
        graduacoes.add(graduacaoAtual);
        LocalDate ultimaGraduacao = dataGraduacao;

        for (FaixasEnum faixaEnum : faixasRestantes){
            Graduacao graduacaoProxFaixa = preencheGraduacaoResposta(aulasPorSemana, faixaEnum, ultimaGraduacao);
            ultimaGraduacao = graduacaoProxFaixa.getGraduacao();
            graduacoes.add(graduacaoProxFaixa);
        }

        return graduacoes;
    }

    private Graduacao preencheGraduacaoResposta(int aulasPorSemana, FaixasEnum faixaEnum, LocalDate ultimaGraduacao) {
        Graduacao graduacaoFaltante = new Graduacao();
        graduacaoFaltante.setFaixa(faixaEnum);
        LocalDate graduacaoProxFaixa = this.adicionarDiasUteis(ultimaGraduacao, faixaEnum.getNumeroTotalAulasGraduacao(), aulasPorSemana);
        graduacaoFaltante.setGraduacao(graduacaoProxFaixa);
        graduacaoFaltante.setAulasProxFaixa(faixaEnum.getNumeroTotalAulasGraduacao());
        graduacaoFaltante.setAulasParaPreta(faixaEnum.getTotalAulasFaltantesPreta(0,0));
        this.preencherDatas(graduacaoFaltante, faixaEnum, ultimaGraduacao, 0, 0, aulasPorSemana);
        return graduacaoFaltante;
    }

    private void preencherDatas(Graduacao graduacao, FaixasEnum faixa, LocalDate dataAtual, int grauAtual, int aulasFeitas, int aulasPorSemana) {
        graduacao.setGrau1(dataAtual);
        graduacao.setGrau2(dataAtual);
        graduacao.setGrau3(dataAtual);
        switch (grauAtual) {
            case 0:
                graduacao.setGrau1(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() -aulasFeitas, aulasPorSemana));
            case 1:
                graduacao.setGrau2(adicionarDiasUteis(graduacao.getGrau1(), faixa.getNumeroAulasGrau(), aulasPorSemana));
            case 2:
                graduacao.setGrau3(adicionarDiasUteis(graduacao.getGrau2(), faixa.getNumeroAulasGrau(), aulasPorSemana));
            case 3:
                graduacao.setGrau4(adicionarDiasUteis(graduacao.getGrau3(), faixa.getNumeroAulasGrau(), aulasPorSemana));
            case 4:
                graduacao.setGraduacao(adicionarDiasUteis(graduacao.getGrau4(), faixa.getNumeroAulasGrau(), aulasPorSemana));
                graduacao.setGraduacaoPreta(adicionarDiasUteis(graduacao.getGraduacao(), faixa.getTotalAulasFaltantesPreta(grauAtual,aulasFeitas), aulasPorSemana));
                graduacao.setGraduacaoCoral(graduacao.getGraduacaoPreta().plusYears(faixa.anosParaCoral(0)));
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
