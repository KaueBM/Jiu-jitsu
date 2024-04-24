package com.dev.jiujitsu.domain.request;

import lombok.Data;

@Data
public class FeriadoRequest {

    private int dia;
    private int mes;
    private int quantidadeDeAnos;
    private String nome;
    private String tipo;

}