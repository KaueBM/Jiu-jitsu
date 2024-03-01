package com.dev.jiujitsu.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Grau {
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate data;
    private String diaDaSemana;

}