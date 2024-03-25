package com.dev.jiujitsu.service;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.dev.jiujitsu.domain.dto.GraduacaoDTO;
import com.dev.jiujitsu.domain.dto.GrauDTO;
import com.dev.jiujitsu.domain.vo.GraduacaoRequest;
import com.dev.jiujitsu.service.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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

    public List<GraduacaoDTO> buscarDatasGraduacoes(GraduacaoRequest request) {
        int aulasPorSemana= request.getAulasPorSemana();
        int aulasFeitas= request.getAulasFeitas();
        int grausRecebidos = request.getGrausRecebidos();
        String faixa = request.getFaixa();
        List<DayOfWeek> diasSemana = dataUtil.obterDiasDaSemana(request.getDiasDasAulas());
        boolean maisDeUmaAulaDia = request.isMaisDeUmaAulaDia();

        FaixasEnum faixaAtual = FaixasEnum.obterFaixaPorNome(faixa);
        int aulasGraduacao = faixaAtual.getAulasRestantesParaProximaFaixa(grausRecebidos, aulasFeitas);
        LocalDate dataGraduacao = adicionarDiasUteis(now(), aulasGraduacao, aulasPorSemana, diasSemana, maisDeUmaAulaDia);
        List<GraduacaoDTO> graduacoes = new ArrayList<>();
        Map<String, GrauDTO> grauAtual = new HashMap<>();

        GraduacaoDTO graduacaoDTOAtual = new GraduacaoDTO();
        graduacaoDTOAtual.setFaixa(faixaAtual);
        graduacaoDTOAtual.setAulasProxFaixa(aulasGraduacao);
        graduacaoDTOAtual.setAulasParaPreta(faixaAtual.getTotalAulasFaltantesPreta(grausRecebidos, aulasFeitas));
        preencherDatas(graduacaoDTOAtual, now(), grausRecebidos, aulasFeitas, aulasPorSemana, grauAtual, false,  diasSemana, maisDeUmaAulaDia);
        graduacoes.add(graduacaoDTOAtual);
        LocalDate ultimaGraduacao = dataGraduacao;

        List<FaixasEnum> faixasRestantes = faixaAtual.buscaListaFaixasQueFaltam(faixa);
        for (FaixasEnum faixaEnum : faixasRestantes) {
            Map<String, GrauDTO> grauProximo = new HashMap<>();
            GraduacaoDTO graduacaoDTOProxFaixa = preencheGraduacaoResposta(aulasPorSemana, faixaEnum, ultimaGraduacao, grauProximo, diasSemana, maisDeUmaAulaDia);
            ultimaGraduacao = graduacaoDTOProxFaixa.buscarDataGrau("Graduação");
            graduacoes.add(graduacaoDTOProxFaixa);
        }

        return graduacoes;
    }

    private GraduacaoDTO preencheGraduacaoResposta(int aulasPorSemana, FaixasEnum faixaEnum, LocalDate ultimaGraduacao, Map<String, GrauDTO> grau, List<DayOfWeek> diasDasAulas, boolean maisDeUmaAulaDia) {
        LocalDate graduacaoProxFaixa = adicionarDiasUteis(ultimaGraduacao, faixaEnum.getNumeroTotalAulasGraduacao(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia);
        GraduacaoDTO graduacaoDTOFaltante = new GraduacaoDTO();

        graduacaoDTOFaltante.setFaixa(faixaEnum);
        grau.put("Graduação", criaGrau(graduacaoProxFaixa));
        graduacaoDTOFaltante.setAulasProxFaixa(faixaEnum.getNumeroTotalAulasGraduacao());
        graduacaoDTOFaltante.setAulasParaPreta(faixaEnum.getTotalAulasFaltantesPreta(0, 0));
        preencherDatas(graduacaoDTOFaltante, ultimaGraduacao, 0, 0, aulasPorSemana, grau, true, diasDasAulas, maisDeUmaAulaDia);

        return graduacaoDTOFaltante;
    }

    private void preencherDatas(GraduacaoDTO graduacaoDTO, LocalDate dataAtual, int grauAtual, int aulasFeitas, int aulasPorSemana, Map<String, GrauDTO> grau, boolean proxFaixa, List<DayOfWeek> diasDasAulas, boolean maisDeUmaAulaDia) {
        FaixasEnum faixa = graduacaoDTO.getFaixa();
        int grauPreta = FaixasEnum.ehPreta(faixa) ? grauAtual : 0;
        aulasPorSemana = aulasFaixaIniciante(faixa, grauAtual, aulasPorSemana);
        diasDasAulas = diaDasAulasIniciante(faixa, grauAtual, diasDasAulas);

        switch (grauAtual) {
            case 0:
                grau.put("1º grau", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("2º grau", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "1º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("3º grau", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "2º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("4º grau", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "3º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("Graduação", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "4º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                break;
            case 1:
                grau.put("2º grau", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("3º grau", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "2º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("4º grau", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "3º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("Graduação", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "4º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                break;
            case 2:
                grau.put("3º grau", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("4º grau", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "3º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("Graduação", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "4º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                break;
            case 3:
                grau.put("4º grau", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                grau.put("Graduação", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "4º grau"), faixa.getNumeroAulasGrau(), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                break;
            case 4:
                grau.put("Graduação", criaGrau(adicionarDiasUteis(dataAtual, faixa.getNumeroAulasGrau() - aulasFeitas, aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));
                break;
        }

        if (!proxFaixa) {
            grau.put("Graduação Preta", criaGrau(adicionarDiasUteis(buscarDataGrau(grau, "Graduação").plusDays(1), faixa.getTotalAulasFaltantesPreta(grauAtual, aulasFeitas), aulasPorSemana, diasDasAulas, maisDeUmaAulaDia)));

            if (FaixasEnum.MARROM.equals(faixa)) {
                grau.replace("Graduação Preta", grau.get("Graduação"));
                grau.remove("Graduação");
            }

            grau.put("Graduação Coral", criaGrau(buscarDataGrau(grau, "Graduação Preta").plusDays(1).plusYears(faixa.anosParaCoral(grauPreta))));
            grau.put("Graduação Vermelha", criaGrau(buscarDataGrau(grau, "Graduação Preta").plusDays(1).plusYears(faixa.anosParaVermelha(grauPreta))));
        }

        graduacaoDTO.setGrau(arrumaGrauPorData(grau));
    }

    private int aulasFaixaIniciante(FaixasEnum faixa, int grauAtual, int aulasPorSemana) {
        if(FaixasEnum.BRANCA.equals(faixa) && grauAtual < 2) return  3;

        return aulasPorSemana;
    }

    private List<DayOfWeek> diaDasAulasIniciante(FaixasEnum faixa, int grauAtual, List<DayOfWeek> diasDasAulas) {
        if(FaixasEnum.BRANCA.equals(faixa) && grauAtual < 2) return Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);

        return diasDasAulas;
    }

    private GrauDTO criaGrau(LocalDate data) {
        GrauDTO grauDTO = new GrauDTO();

        data = dataUtil.pulaFinaisDeSemanaFeriadosRecessos(data);
        grauDTO.setData(data);
        grauDTO.setDiaDaSemana(dataUtil.retornaDiaDaSemana(data.getDayOfWeek()));
        return grauDTO;
    }

    private LocalDate adicionarDiasUteis(LocalDate data, int numeroDias, int diasDeAulasPorSemana,  List<DayOfWeek> diasDasAulas, boolean maisDeUmaAulaDia) {
        int diasAdicionados = 0;
        int diaMesmaSemana = 0;

        while (diasAdicionados < numeroDias) {
            if (dataUtil.ehFinalDeSemana(data)) {
                diaMesmaSemana = 0;
            }
            data = dataUtil.pulaFinaisDeSemanaFeriadosRecessos(data);
            data = dataUtil.ajustaParaDiaDeAulaDoAluno(data, diasDeAulasPorSemana, diasDasAulas);

            if(maisDeUmaAulaDia){
                diasAdicionados+=2;
            } else{
                diasAdicionados++;
            }

            diaMesmaSemana++;

            if (diaMesmaSemana > diasDeAulasPorSemana && dataUtil.diaAdicionadoEhMenor(numeroDias, diasAdicionados)) {
                data = dataUtil.pulaSemanaAposLimite(data);
                data = dataUtil.ajustaParaDiaDeAulaDoAluno(data, diasDeAulasPorSemana, diasDasAulas);
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