package com.dev.jiujitsu.converter;

import com.dev.jiujitsu.domain.dto.Feriado;
import com.dev.jiujitsu.domain.request.FeriadoRequest;
import com.dev.jiujitsu.domain.vo.FeriadosResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FeriadoConverter {

    public Feriado toDomain(FeriadosResponse response) {
        return Feriado.builder()
                .nome(response.getNome())
                .data(response.getData())
                .tipo(response.getTipo())
                .build();
    }

    public Feriado toDomain(FeriadoRequest request, LocalDate diaFeriado) {
        return Feriado.builder()
                .nome(request.getNome())
                .tipo(request.getTipo())
                .data(diaFeriado)
                .build();
    }

}