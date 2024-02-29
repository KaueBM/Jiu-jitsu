package com.dev.jiujitsu.service;

import com.dev.jiujitsu.converter.FeriadoConverter;
import com.dev.jiujitsu.domain.dto.Feriado;
import com.dev.jiujitsu.domain.vo.FeriadosResponse;
import com.dev.jiujitsu.external.FeriadosClient;
import com.dev.jiujitsu.repository.FeriadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeriadoService {

    @Autowired
    private FeriadoRepository repository;

    @Autowired
    private FeriadosClient client;

    @Autowired
    private FeriadoConverter converter;

    @Autowired
    public FeriadoService(FeriadoRepository feriadoRepository) {
        this.repository = feriadoRepository;
    }

    public void salvarFeriados(int ano){
        List<FeriadosResponse> feriados = client.getFeriados(ano);

        feriados.forEach(feriado -> repository.save(converter.toDomain(feriado)));
    }

    public boolean ehFeriado(LocalDate data) {
        Feriado feriado = repository.findTopByOrderByDataDesc();
        if(data.isAfter(feriado.getData())) return false;

        return CollectionUtils.isEmpty(repository.findByData(data));
    }

}