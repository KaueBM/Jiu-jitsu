package com.dev.jiujitsu.domain.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FeriadosResponse {

    @JsonAlias(value = "date")
    private LocalDate data;

    @JsonAlias(value = "name")
    private String nome;

    @JsonAlias(value = "type")
    private String tipo;

}