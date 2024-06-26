package com.dev.jiujitsu.service;

import com.dev.jiujitsu.converter.FeriadoConverter;
import com.dev.jiujitsu.domain.dto.Feriado;
import com.dev.jiujitsu.domain.request.FeriadoRequest;
import com.dev.jiujitsu.domain.vo.FeriadosResponse;
import com.dev.jiujitsu.external.FeriadosClient;
import com.dev.jiujitsu.repository.FeriadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

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

    public void salvarFeriados(int ano) {
        List<FeriadosResponse> feriados = client.getFeriados(ano);

        feriados.forEach(feriado -> repository.save(converter.toDomain(feriado)));
    }

    public boolean ehFeriado(LocalDate data) {
        Feriado feriado = repository.findTopByOrderByDataDesc();
        if (data.getYear() > feriado.getData().getYear()) {
            this.salvarFeriados(data.getYear());
        }
        return !isEmpty(repository.findByData(data));
    }

    public void removerFeriados(String id) {
        repository.deleteById(id);
    }

    public void salvaFeriadoPorPeriodo(FeriadoRequest request) {
        int ano = LocalDate.now().getYear();
        for (int i = 0; i < request.getQuantidadeDeAnos(); i++) {
            LocalDate diaFeriado = LocalDate.of(ano + i, request.getMes(), request.getDia());

            repository.save(converter.toDomain(request, diaFeriado));
        }
    }

}