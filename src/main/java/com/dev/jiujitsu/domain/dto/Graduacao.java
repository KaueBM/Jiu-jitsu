package com.dev.jiujitsu.domain.dto;

import com.dev.jiujitsu.constants.enums.FaixasEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Graduacao {

    private FaixasEnum faixa;
    private int aulasProxFaixa;
    private int aulasParaPreta;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate grau1;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate grau2;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate grau3;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate grau4;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate graduacao;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate graduacaoPreta;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate graduacaoCoral;
}
