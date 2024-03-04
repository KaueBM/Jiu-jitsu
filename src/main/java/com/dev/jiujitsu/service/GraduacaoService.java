package com.dev.jiujitsu.service;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.dev.jiujitsu.domain.dto.GraduacaoDTO;
import com.dev.jiujitsu.domain.dto.GrauDTO;
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

    @Autowired
    private DataUtil dataUtil;

    public List<GraduacaoDTO> buscarDatasGraduacoes(String faixa, int aulasPorSemana, int aulasFeitas, int grausRecebidos) {
        FaixasEnum faixaAtual = FaixasEnum.obterFaixaPorNome(faixa);
        int aulasGraduacao = faixaAtual.getAulasRestantesParaProximaFaixa(grausRecebidos, aulasFeitas);
        LocalDate dataGraduacao = this.adicionarDiasUteis(now(), aulasGraduacao, aulasPorSemana);
        List<GraduacaoDTO> graduacoes = new ArrayList<>();
        Map<String, GrauDTO> grauAtual = new HashMap<>();

        GraduacaoDTO graduacaoDTOAtual = new GraduacaoDTO();
        graduacaoDTOAtual.setFaixa(faixaAtual);
        graduacaoDTOAtual.setAulasProxFaixa(aulasGraduacao);
        graduacaoDTOAtual.setAulasParaPreta(faixaAtual.getTotalAulasFaltantesPreta(grausRecebidos, aulasFeitas));
        this.preencherDatas(graduacaoDTOAtual, now(), grausRecebidos, aulasFeitas, aulasPorSemana, grauAtual, false);
        graduacoes.add(graduacaoDTOAtual);
        LocalDate ultimaGraduacao = dataGraduacao;

        List<FaixasEnum> faixasRestantes = faixaAtual.buscaListaFaixasQueFaltam(faixa);
        for (FaixasEnum faixaEnum : faixasRestantes) {
            Map<String, GrauDTO> grauProximo = new HashMap<>();
            GraduacaoDTO graduacaoDTOProxFaixa = preencheGraduacaoResposta(aulasPorSemana, faixaEnum, ultimaGraduacao, grauProximo);
            ultimaGraduacao = graduacaoDTOProxFaixa.buscarDataGrau("Graduação");
            graduacoes.add(graduacaoDTOProxFaixa);
        }

        return graduacoes;
    }

    private GraduacaoDTO preencheGraduacaoResposta(int aulasPorSemana, FaixasEnum faixaEnum, LocalDate ultimaGraduacao, Map<String, GrauDTO> grau) {
        LocalDate graduacaoProxFaixa = this.adicionarDiasUteis(ultimaGraduacao, faixaEnum.getNumeroTotalAulasGraduacao(), aulasPorSemana);
        GraduacaoDTO graduacaoDTOFaltante = new GraduacaoDTO();

        graduacaoDTOFaltante.setFaixa(faixaEnum);
        grau.put("Graduação", criaGrau(graduacaoProxFaixa));
        graduacaoDTOFaltante.setAulasProxFaixa(faixaEnum.getNumeroTotalAulasGraduacao());
        graduacaoDTOFaltante.setAulasParaPreta(faixaEnum.getTotalAulasFaltantesPreta(0, 0));
        this.preencherDatas(graduacaoDTOFaltante, ultimaGraduacao, 0, 0, aulasPorSemana, grau, true);

        return graduacaoDTOFaltante;
    }

    private void preencherDatas(GraduacaoDTO graduacaoDTO, LocalDate dataAtual, int grauAtual, int aulasFeitas, int aulasPorSemana, Map<String, GrauDTO> grau, boolean proxFaixa) {
        FaixasEnum faixa = graduacaoDTO.getFaixa();
        int grauPreta = FaixasEnum.ehPreta(faixa) ? grauAtual : 0;

        switch (grauAtual) {
            case 0:
                grau.put("1º grau", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                grau.put("2º grau", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "1º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put("3º grau", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "2º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put("4º grau", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "3º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put("Graduação", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "4º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                break;
            case 1:
                grau.put("2º grau", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                grau.put("3º grau", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "2º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put("4º grau", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "3º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put("Graduação", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "4º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                break;
            case 2:
                grau.put("3º grau", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                grau.put("4º grau", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "3º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                grau.put("Graduação", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "4º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                break;
            case 3:
                grau.put("4º grau", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                grau.put("Graduação", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "4º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana)));
                break;
            case 4:
                grau.put("Graduação", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana)));
                break;
        }

        if (!proxFaixa) {
            grau.put("Graduação Preta", criaGrau(adicionarDiasUteis(this.buscarDataGrau(grau, "Graduação").plusDays(1), faixa.getTotalAulasFaltantesPreta(grauAtual, aulasFeitas), aulasPorSemana)));

            if (FaixasEnum.MARROM.equals(faixa)) {
                grau.replace("Graduação Preta", grau.get("Graduação"));
                grau.remove("Graduação");
            }

            grau.put("Graduação Coral", criaGrau(this.buscarDataGrau(grau, "Graduação Preta").plusDays(1).plusYears(faixa.anosParaCoral(grauPreta))));
            grau.put("Graduação Vermelha", criaGrau(this.buscarDataGrau(grau, "Graduação Preta").plusDays(1).plusYears(faixa.anosParaVermelha(grauPreta))));
        }

        graduacaoDTO.setGrau(arrumaGrauPorData(grau));
    }

    private GrauDTO criaGrau(LocalDate data) {
        GrauDTO grauDTO = new GrauDTO();

        data = dataUtil.pulaFinaisDeSemanaFeriadosRecessos(data);
        grauDTO.setData(data);
        grauDTO.setDiaDaSemana(dataUtil.retornaDiaDaSemana(data.getDayOfWeek()));
        return grauDTO;
    }

    private LocalDate adicionarDiasUteis(LocalDate data, int numeroDias, int diasUteisPorSemana) {
        int diasAdicionados = 0;
        int diaMesmaSemana = 0;

        while (diasAdicionados < numeroDias) {
            if (dataUtil.ehFinalDeSemana(data)) {
                diaMesmaSemana = 0;
            }
            data = dataUtil.pulaFinaisDeSemanaFeriadosRecessos(data);
            diasAdicionados++;
            diaMesmaSemana++;

            if (diaMesmaSemana > diasUteisPorSemana && dataUtil.diaAdicionadoEhMenor(numeroDias, diasAdicionados)) {
                data = dataUtil.pulaSemanaAposLimite(data);
                diaMesmaSemana = 0;
            } else if (dataUtil.diaAdicionadoEhMenor(numeroDias, diasAdicionados)) {
                data = data.plusDays(1);
            }
        }

        return data;
    }

    private LocalDate buscarDataGrau(Map<String, GrauDTO> graduacao, String grau) {
        return Objects.isNull(graduacao.get(grau)) ? LocalDate.now() : graduacao.get(grau).getData();
    }

    private static Map<String, GrauDTO> arrumaGrauPorData(Map<String, GrauDTO> unsortedMap) {
        List<Map.Entry<String, GrauDTO>> list = new LinkedList<>(unsortedMap.entrySet());

        list.sort(Comparator.comparing(entry -> entry.getValue().getData()));

        Map<String, GrauDTO> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, GrauDTO> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}