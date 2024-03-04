package com.dev.jiujitsu.domain.dto;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Data
public class GraduacaoDTO {

    private FaixasEnum faixa;
    private int aulasProxFaixa;
    private int aulasParaPreta;
    private Map<String, GrauDTO> grau;


    public LocalDate buscarDataGrau(String grau) {
        return Objects.isNull(this.getGrau().get(grau)) ? LocalDate.now() : this.getGrau().get(grau).getData();
    }

}