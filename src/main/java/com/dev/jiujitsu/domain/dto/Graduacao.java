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
    private Grau grau1;
    private Grau grau2;
    private Grau grau3;
    private Grau grau4;
    private Grau graduacao;
    private Grau graduacaoPreta;
    private Grau graduacaoCoral;
    private Grau graduacaoVermelha;

    @Data
    public static class Grau {
        @JsonFormat(pattern="dd/MM/yyyy")
        private LocalDate data;
        private String diaDaSemana;
    }
}
