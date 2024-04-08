package com.dev.jiujitsu.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@Document(collection = "recessos")
public class Recesso {

    @Id
    private String id;
    private LocalDate data;
    private String nome;
    private String descricao;
}
