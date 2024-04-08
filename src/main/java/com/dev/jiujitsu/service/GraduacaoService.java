package com.dev.jiujitsu.service;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.dev.jiujitsu.domain.dto.Graduacao;
import com.dev.jiujitsu.domain.dto.Grau;
import com.dev.jiujitsu.domain.request.GraduacaoRequest;
import com.dev.jiujitsu.service.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.time.LocalDate.now;

@Service
public class GraduacaoService {

    public static final String PRIMEIRO_GRAU = "1º grau";
    public static final String SEGUNDO_GRAU = "2º grau";
    public static final String TERCEIRO_GRAU = "3º grau";
    public static final String QUARTO_GRAU = "4º grau";
    public static final String GRADUACAO = "Graduação";
    public static final String GRADUACAO_PRETA = "Graduação Preta";

    @Autowired private DataUtil dataUtil;

    public List<Graduacao> buscarDatasGraduacoes( GraduacaoRequest request){
        String faixa = request.getFaixa();
        int aulasPorSemana = request.getAulasPorSemana();
        int aulasFeitas = request.getAulasFeitas();
        int grausRecebidos = request.getGrausRecebidos();
        FaixasEnum faixaAtual = FaixasEnum.obterFaixaPorNome(faixa);
        int aulasGraduacao = faixaAtual.getAulasRestantesParaProximaFaixa(grausRecebidos,aulasFeitas);
        LocalDate dataGraduacao = dataUtil.adicionarDiasUteis(now(),aulasGraduacao, aulasPorSemana);
        List<Graduacao> graduacoes = new ArrayList<>();
        Map<String, Grau> grauAtual = new HashMap<>();

        Graduacao graduacaoAtual = new Graduacao();
        graduacaoAtual.setFaixa(faixaAtual);
        graduacaoAtual.setAulasProxFaixa(aulasGraduacao);
        graduacaoAtual.setAulasParaPreta(faixaAtual.getTotalAulasFaltantesPreta(grausRecebidos,aulasFeitas));
        this.preencherDatas(graduacaoAtual, now(), grausRecebidos, aulasFeitas, aulasPorSemana, grauAtual, false);
        graduacoes.add(graduacaoAtual);
        LocalDate ultimaGraduacao = dataGraduacao;

        List<FaixasEnum> faixasRestantes = faixaAtual.buscaListaFaixasQueFaltam(faixa);
        for (FaixasEnum faixaEnum : faixasRestantes){
            Map<String, Grau> grauProximo = new HashMap<>();
            Graduacao graduacaoProxFaixa = preencheGraduacaoResposta(aulasPorSemana, faixaEnum, ultimaGraduacao, grauProximo);
            ultimaGraduacao = graduacaoProxFaixa.buscarDataGrau("Graduação");
            graduacoes.add(graduacaoProxFaixa);
        }

        return graduacoes;
    }

    private Graduacao preencheGraduacaoResposta(int aulasPorSemana, FaixasEnum faixaEnum, LocalDate ultimaGraduacao, Map<String, Grau> grau) {
        LocalDate graduacaoProxFaixa = dataUtil.adicionarDiasUteis(ultimaGraduacao, faixaEnum.getNumeroTotalAulasGraduacao(), aulasPorSemana);
        Graduacao graduacaoFaltante = new Graduacao();

        graduacaoFaltante.setFaixa(faixaEnum);
        grau.put("Graduação",criaGrau(graduacaoProxFaixa));
        graduacaoFaltante.setAulasProxFaixa(faixaEnum.getNumeroTotalAulasGraduacao());
        graduacaoFaltante.setAulasParaPreta(faixaEnum.getTotalAulasFaltantesPreta(0,0));
        this.preencherDatas(graduacaoFaltante, ultimaGraduacao, 0, 0, aulasPorSemana, grau, true);

        return graduacaoFaltante;
    }

    private void preencherDatas(Graduacao graduacao, LocalDate dataAtual, int grauAtual, int aulasFeitas, int aulasPorSemana, Map<String, Grau> grau, boolean proxFaixa) {
        FaixasEnum faixa = graduacao.getFaixa();
        int grauPreta = FaixasEnum.ehPreta(faixa) ? grauAtual : 0;

        switch (grauAtual) {
            case 0:
                grau.put(PRIMEIRO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() -aulasFeitas, aulasPorSemana)));
                grau.put(SEGUNDO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,PRIMEIRO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put(TERCEIRO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,SEGUNDO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put(QUARTO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,TERCEIRO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put(GRADUACAO, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,QUARTO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                break;
            case 1:
                grau.put(SEGUNDO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                grau.put(TERCEIRO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,SEGUNDO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put(QUARTO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,TERCEIRO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put(GRADUACAO, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,QUARTO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                break;
            case 2:
                grau.put(TERCEIRO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                grau.put(QUARTO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,TERCEIRO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put(GRADUACAO, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,QUARTO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                break;
            case 3:
                grau.put(QUARTO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                grau.put(GRADUACAO, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,QUARTO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                break;
            case 4:
                grau.put(GRADUACAO, criaGrau(dataUtil.adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                break;
        }

        if(!proxFaixa){
            grau.put(GRADUACAO_PRETA, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau,GRADUACAO).plusDays(1), faixa.getTotalAulasFaltantesPreta(grauAtual,aulasFeitas), aulasPorSemana)));

            if(FaixasEnum.MARROM.equals(faixa)){
                grau.replace(GRADUACAO_PRETA, grau.get(GRADUACAO));
                grau.remove(GRADUACAO);
            }

            grau.put("Graduação Coral",criaGrau(this.buscarDataGrau(grau,GRADUACAO_PRETA).plusDays(1).plusYears(faixa.anosParaCoral(grauPreta))));
            grau.put("Graduação Vermelha",criaGrau(this.buscarDataGrau(grau,GRADUACAO_PRETA).plusDays(1).plusYears(faixa.anosParaVermelha(grauPreta))));
        }

        graduacao.setGrau(arrumaGrauPorData(grau));
    }

    private Grau criaGrau(LocalDate data){
        Grau grau = new Grau();

        data = dataUtil.pulaFinaisDeSemanaFeriadosRecessos(data);
        grau.setData(data);
        grau.setDiaDaSemana(dataUtil.retornaDiaDaSemana(data.getDayOfWeek()));
        return grau;
    }



    private LocalDate buscarDataGrau(Map<String, Grau> graduacao, String grau){
        return Objects.isNull(graduacao.get(grau)) ? LocalDate.now() : graduacao.get(grau).getData();
    }

    private static Map<String, Grau> arrumaGrauPorData(Map<String, Grau> unsortedMap) {
        List<Map.Entry<String, Grau>> list = new LinkedList<>(unsortedMap.entrySet());

        list.sort(Comparator.comparing(entry -> entry.getValue().getData()));

        Map<String, Grau> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Grau> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }



}