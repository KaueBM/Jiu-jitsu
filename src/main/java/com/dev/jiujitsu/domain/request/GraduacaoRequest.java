package com.dev.jiujitsu.domain.request;

import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;

@Data
public class GraduacaoRequest {

    private String faixa;
    private int aulasPorSemana;
    private int aulasFeitas;
    private  int grausRecebidos;

    private List<DayOfWeek> diasDasAulas;

    private boolean maisDeUmaAulaDia;
}
