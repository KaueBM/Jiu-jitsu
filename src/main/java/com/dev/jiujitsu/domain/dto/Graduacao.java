package com.dev.jiujitsu.domain.dto;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Data
public class Graduacao {

    private FaixasEnum faixa;
    private int aulasProxFaixa;
    private int aulasParaPreta;
    private Map<String, Grau> grau;


    public LocalDate buscarDataGrau(String grau){
        return Objects.isNull(this.getGrau().get(grau)) ? LocalDate.now() :this.getGrau().get(grau).getData();
    }

    public void setarDataGraduacao(String graduacao, LocalDate data){
        this.getGrau().get(graduacao).setData(data);
    }
}
