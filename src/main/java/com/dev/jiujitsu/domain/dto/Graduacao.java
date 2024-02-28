package com.dev.jiujitsu.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Graduacao {

    private int aulasFaltantes;
    private LocalDate grau1;
    private LocalDate grau2;
    private LocalDate grau3;
    private LocalDate grau4;
    private LocalDate graduacao;
    private LocalDate graduacaoPreta;
    private LocalDate graduacaoCoral;
}
