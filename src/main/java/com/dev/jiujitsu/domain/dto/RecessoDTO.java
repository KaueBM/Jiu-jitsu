package com.dev.jiujitsu.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecessoDTO {

    private List<Integer> dias;
    private int mes;
    private int ano;
    private String nome;
    private String descricao;

}