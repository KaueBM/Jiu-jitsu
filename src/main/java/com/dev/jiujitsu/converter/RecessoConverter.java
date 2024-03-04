package com.dev.jiujitsu.converter;

import com.dev.jiujitsu.domain.dto.RecessoDTO;
import com.dev.jiujitsu.domain.entity.Recesso;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RecessoConverter {

    public Recesso toDomain(RecessoDTO dto, int dia) {
        LocalDate data = LocalDate.of(dto.getAno(), dto.getMes(), dia);
        return Recesso.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .data(data)
                .build();
    }

}