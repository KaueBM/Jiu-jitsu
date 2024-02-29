package com.dev.jiujitsu.service;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.dev.jiujitsu.domain.dto.Graduacao;
import com.dev.jiujitsu.service.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;

@Service
public class GraduacaoService {

    @Autowired private DataUtil dataUtil;

    public List<Graduacao> buscarDatasGraduacoes(String faixa, int aulasPorSemana, int aulasFeitas, int grausRecebidos){
        FaixasEnum faixaAtual = FaixasEnum.obterFaixaPorNome(faixa);
        int aulasGraduacao = faixaAtual.getAulasRestantesParaProximaFaixa(grausRecebidos,aulasFeitas);
        LocalDate dataGraduacao = this.adicionarDiasUteis(now(),aulasGraduacao, aulasPorSemana);
        List<Graduacao> graduacoes = new ArrayList<>();

        Graduacao graduacaoAtual = new Graduacao();
        graduacaoAtual.setFaixa(faixaAtual);
        graduacaoAtual.setGraduacao(criaGrau(dataGraduacao));
        graduacaoAtual.setAulasProxFaixa(aulasGraduacao);
        graduacaoAtual.setAulasParaPreta(faixaAtual.getTotalAulasFaltantesPreta(grausRecebidos,aulasFeitas));
        this.preencherDatas(graduacaoAtual, now(), grausRecebidos, aulasFeitas, aulasPorSemana);

        graduacoes.add(graduacaoAtual);
        LocalDate ultimaGraduacao = dataGraduacao;

        List<FaixasEnum> faixasRestantes = faixaAtual.buscaListaFaixasQueFaltam(faixa);
        for (FaixasEnum faixaEnum : faixasRestantes){
            Graduacao graduacaoProxFaixa = preencheGraduacaoResposta(aulasPorSemana, faixaEnum, ultimaGraduacao);
            ultimaGraduacao = graduacaoProxFaixa.getGraduacao().getData();
            graduacoes.add(graduacaoProxFaixa);
        }

        return graduacoes;
    }

    private Graduacao preencheGraduacaoResposta(int aulasPorSemana, FaixasEnum faixaEnum, LocalDate ultimaGraduacao) {
        LocalDate graduacaoProxFaixa = this.adicionarDiasUteis(ultimaGraduacao, faixaEnum.getNumeroTotalAulasGraduacao(), aulasPorSemana);
        Graduacao graduacaoFaltante = new Graduacao();

        graduacaoFaltante.setFaixa(faixaEnum);
        graduacaoFaltante.setGraduacao(criaGrau(graduacaoProxFaixa));
        graduacaoFaltante.setAulasProxFaixa(faixaEnum.getNumeroTotalAulasGraduacao());
        graduacaoFaltante.setAulasParaPreta(faixaEnum.getTotalAulasFaltantesPreta(0,0));
        this.preencherDatas(graduacaoFaltante, ultimaGraduacao, 0, 0, aulasPorSemana);

        return graduacaoFaltante;
    }

    private void preencherDatas(Graduacao graduacao, LocalDate dataAtual, int grauAtual, int aulasFeitas, int aulasPorSemana) {
        FaixasEnum faixa = graduacao.getFaixa();
        int grauPreta = FaixasEnum.ehPreta(faixa) ? grauAtual : 0;

        graduacao.setGrau1(criaGrau(dataAtual));
        graduacao.setGrau2(criaGrau(dataAtual));
        graduacao.setGrau3(criaGrau(dataAtual));
        graduacao.setGrau4(criaGrau(dataAtual));

        switch (grauAtual) {
            case 0:
                graduacao.setGrau1(criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() -aulasFeitas, aulasPorSemana)));
                graduacao.setGrau2(criaGrau(adicionarDiasUteis(graduacao.getGrau1().getData(), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                graduacao.setGrau3(criaGrau(adicionarDiasUteis(graduacao.getGrau2().getData(), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                graduacao.setGrau4(criaGrau(adicionarDiasUteis(graduacao.getGrau3().getData(), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                graduacao.setGraduacao(criaGrau(adicionarDiasUteis(graduacao.getGrau4().getData(), faixa.getNumeroAulasGraduacao(), aulasPorSemana)));
                break;
            case 1:
                graduacao.setGrau2(criaGrau(adicionarDiasUteis(graduacao.getGrau1().getData(), faixa.getNumeroAulasGrau()-aulasFeitas, aulasPorSemana)));
                graduacao.setGrau3(criaGrau(adicionarDiasUteis(graduacao.getGrau2().getData().plusDays(1), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                graduacao.setGrau4(criaGrau(adicionarDiasUteis(graduacao.getGrau3().getData().plusDays(1), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                graduacao.setGraduacao(criaGrau(adicionarDiasUteis(graduacao.getGrau4().getData().plusDays(1), faixa.getNumeroAulasGraduacao(), aulasPorSemana)));
                break;
            case 2:
                graduacao.setGrau3(criaGrau(adicionarDiasUteis(graduacao.getGrau2().getData(), faixa.getNumeroAulasGrau()-aulasFeitas, aulasPorSemana)));
                graduacao.setGrau4(criaGrau(adicionarDiasUteis(graduacao.getGrau3().getData().plusDays(1), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                graduacao.setGraduacao(criaGrau(adicionarDiasUteis(graduacao.getGrau4().getData().plusDays(1), faixa.getNumeroAulasGraduacao(), aulasPorSemana)));
                break;
            case 3:
                graduacao.setGrau4(criaGrau(adicionarDiasUteis(graduacao.getGrau3().getData(), faixa.getNumeroAulasGrau()-aulasFeitas, aulasPorSemana)));
                graduacao.setGraduacao(criaGrau(adicionarDiasUteis(graduacao.getGrau4().getData().plusDays(1), faixa.getNumeroAulasGraduacao(), aulasPorSemana)));
                break;
            case 4:
                graduacao.setGraduacao(criaGrau(adicionarDiasUteis(graduacao.getGrau4().getData(), faixa.getNumeroAulasGraduacao()-aulasFeitas, aulasPorSemana)));
                break;
        }

        graduacao.setGraduacaoPreta(criaGrau(adicionarDiasUteis(graduacao.getGraduacao().getData().plusDays(1), faixa.getTotalAulasFaltantesPreta(grauAtual,aulasFeitas), aulasPorSemana)));
        graduacao.setGraduacaoCoral(criaGrau(graduacao.getGraduacaoPreta().getData().plusDays(1).plusYears(faixa.anosParaCoral(grauPreta))));
        graduacao.setGraduacaoVermelha(criaGrau(graduacao.getGraduacaoPreta().getData().plusDays(1).plusYears(faixa.anosParaVermelha(grauPreta))));
    }

    private Graduacao.Grau criaGrau(LocalDate data){
        Graduacao.Grau grau = new Graduacao.Grau();

        data = dataUtil.pulaFinaisDeSemanaFeriados(data);
        grau.setData(data);
        grau.setDiaDaSemana(dataUtil.retornaDiaDaSemana(data.getDayOfWeek()));
        return grau;
    }

    private LocalDate adicionarDiasUteis(LocalDate data, int numeroDias, int diasUteisPorSemana) {
        int diasAdicionados = 0;
        int diaMesmaSemana = 0;

        while (diasAdicionados < numeroDias) {
            if (dataUtil.ehFinalDeSemana(data)) {
                diaMesmaSemana = 0;
            }

            data = dataUtil.pulaFinaisDeSemanaFeriados(data);
            diasAdicionados++;
            diaMesmaSemana++;

            if (diaMesmaSemana == diasUteisPorSemana && dataUtil.diaAdicionadoEhMenor(numeroDias, diasAdicionados)) {
                data = dataUtil.pulaSemanaAposLimite(data);
                diaMesmaSemana = 0;
            } else if (dataUtil.diaAdicionadoEhMenor(numeroDias, diasAdicionados)) {
                data = data.plusDays(1);
            }
        }

        return data;
    }

}