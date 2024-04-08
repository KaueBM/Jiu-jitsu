package com.dev.jiujitsu.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class RecessoRequest {

    private List<Integer> dias;
    private int mes;
    private int ano;
    private String nome;
    private String descricao;

}