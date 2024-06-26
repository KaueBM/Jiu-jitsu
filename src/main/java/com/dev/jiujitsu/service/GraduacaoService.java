package com.dev.jiujitsu.service;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.dev.jiujitsu.domain.dto.Graduacao;
import com.dev.jiujitsu.domain.dto.Grau;
import com.dev.jiujitsu.domain.request.GraduacaoRequest;
import com.dev.jiujitsu.service.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;

@Service
public class GraduacaoService {

    private static final String PRIMEIRO_GRAU = "1º grau";
    private static final String SEGUNDO_GRAU = "2º grau";
    private static final String TERCEIRO_GRAU = "3º grau";
    private static final String QUARTO_GRAU = "4º grau";
    private static final String GRADUACAO = "Graduação";
    private static final String GRADUACAO_PRETA = "Graduação Preta";

    private static final int[] graus = {0, 1, 2, 3, 4};

    @Autowired
    private DataUtil dataUtil;

    public List<Graduacao> buscarDatasGraduacoes(GraduacaoRequest request) {
        String faixa = request.getFaixa();
        int aulasPorSemana = request.getAulasPorSemana();
        int aulasFeitas = request.getAulasFeitas();
        int grausRecebidos = request.getGrausRecebidos();
        FaixasEnum faixaAtual = FaixasEnum.obterFaixaPorNome(faixa);
        List<Graduacao> graduacoes = new ArrayList<>();

        Graduacao graduacaoAtual = preencheGraduacaoResposta(aulasPorSemana, faixaAtual, now(), false, grausRecebidos, aulasFeitas);
        graduacoes.add(graduacaoAtual);

        LocalDate ultimaGraduacao = graduacaoAtual.buscarDataGrau(GRADUACAO);

        for (FaixasEnum faixaEnum : faixaAtual.buscaListaFaixasQueFaltam(faixa)) {
            Graduacao graduacaoProxFaixa = preencheGraduacaoResposta(aulasPorSemana, faixaEnum, ultimaGraduacao, true, 0, 0);
            ultimaGraduacao = graduacaoProxFaixa.buscarDataGrau(GRADUACAO);
            graduacoes.add(graduacaoProxFaixa);
        }

        return graduacoes;
    }

    private Graduacao preencheGraduacaoResposta(int aulasPorSemana, FaixasEnum faixa, LocalDate ultimaGraduacao, boolean proxFaixa, int grausRecebidos, int aulasFeitas) {
        grausRecebidos = proxFaixa ? 0 : grausRecebidos;
        aulasFeitas = proxFaixa ? 0 : aulasFeitas;
        Graduacao graduacao = new Graduacao();
        graduacao.setFaixa(faixa);
        graduacao.setAulasProxFaixa(faixa.getAulasRestantesParaProximaFaixa(grausRecebidos, aulasFeitas));
        graduacao.setAulasParaPreta(faixa.getTotalAulasFaltantesPreta(grausRecebidos, aulasFeitas));
        this.preencherDatas(graduacao, ultimaGraduacao, grausRecebidos, aulasFeitas, aulasPorSemana, proxFaixa);

        return graduacao;
    }

    private void preencherDatas(Graduacao graduacao, LocalDate dataAtual, int grauAtual, int aulasFeitas, int aulasPorSemana, boolean proxFaixa) {
        Map<String, Grau> grau = new HashMap<>();
        FaixasEnum faixa = graduacao.getFaixa();
        int grauPreta = FaixasEnum.ehPreta(faixa) ? grauAtual : 0;

        for (int i = grauAtual; i < graus.length; i++) {
            switch (graus[i]) {
                case 0:
                    grau.put(PRIMEIRO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                    break;
                case 1:
                    grau.put(SEGUNDO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau, PRIMEIRO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                    break;
                case 2:
                    grau.put(TERCEIRO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau, SEGUNDO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                    break;
                case 3:
                    grau.put(QUARTO_GRAU, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau, TERCEIRO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                    break;
                case 4:
                    grau.put(GRADUACAO, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau, QUARTO_GRAU), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                    break;
            }
        }

        grau.put(GRADUACAO_PRETA, criaGrau(dataUtil.adicionarDiasUteis(this.buscarDataGrau(grau, GRADUACAO).plusDays(1), faixa.getTotalAulasFaltantesPreta(grauAtual, aulasFeitas), aulasPorSemana)));
        grau.put("Graduação Coral", criaGrau(this.buscarDataGrau(grau, GRADUACAO_PRETA).plusDays(1).plusYears(faixa.anosParaCoral(grauPreta))));
        grau.put("Graduação Vermelha", criaGrau(this.buscarDataGrau(grau, GRADUACAO_PRETA).plusDays(1).plusYears(faixa.anosParaVermelha(grauPreta))));

        if (FaixasEnum.MARROM.equals(faixa)) {
            grau.replace(GRADUACAO_PRETA, grau.get(GRADUACAO));
            grau.remove(GRADUACAO);
        }

        if (proxFaixa) {
            grau.remove(GRADUACAO_PRETA);
            grau.remove("Graduação Coral");
            grau.remove("Graduação Vermelha");
        }

        graduacao.setGrau(arrumaGrauPorData(grau));
    }

    private Grau criaGrau(LocalDate data) {
        LocalDate dataCorrigida = dataUtil.pulaFinaisDeSemanaFeriadosRecessos(data);
        DayOfWeek diaDaSemana = dataCorrigida.getDayOfWeek();

        return new Grau(dataCorrigida, dataUtil.retornaDiaDaSemana(diaDaSemana));
    }


    private LocalDate buscarDataGrau(Map<String, Grau> graduacao, String grau) {
        return graduacao.containsKey(grau) ? graduacao.get(grau).getData() : LocalDate.now();
    }

    private static Map<String, Grau> arrumaGrauPorData(Map<String, Grau> unsortedMap) {
        return unsortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(Grau::getData)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

}