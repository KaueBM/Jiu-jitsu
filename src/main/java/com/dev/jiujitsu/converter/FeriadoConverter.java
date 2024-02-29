package com.dev.jiujitsu.converter;

import com.dev.jiujitsu.domain.dto.Feriado;
import com.dev.jiujitsu.domain.vo.FeriadosResponse;
import org.springframework.stereotype.Component;

@Component
public class FeriadoConverter {

    public Feriado toDomain(FeriadosResponse response) {
        return Feriado.builder()
                .nome(response.getNome())
                .data(response.getData())
                .tipo(response.getTipo())
                .build();
    }
}
