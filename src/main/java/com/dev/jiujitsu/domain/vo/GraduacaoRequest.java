package com.dev.jiujitsu.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class GraduacaoRequest {

    String faixa;
    int aulasPorSemana;
    int aulasFeitas;
    int grausRecebidos;
    List<String> diasDasAulas;
    boolean maisDeUmaAulaDia;

}