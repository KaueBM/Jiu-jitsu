package com.dev.jiujitsu.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DiasLivres {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate diaInicial;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate diaFinal;
    private int diasLivres;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate proximoDiaUtil;

}