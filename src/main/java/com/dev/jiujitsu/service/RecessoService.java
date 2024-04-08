package com.dev.jiujitsu.service;

import com.dev.jiujitsu.domain.dto.Recesso;
import com.dev.jiujitsu.domain.request.RecessoRequest;
import com.dev.jiujitsu.repository.RecessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class RecessoService {

    @Autowired
    private RecessoRepository repository;

    public void salvarRecessos(RecessoRequest request) {
        request.getDias().forEach(dia ->{
            Recesso recesso = Recesso.builder()
                    .nome(request.getNome())
                    .descricao(request.getDescricao())
                    .data(LocalDate.of(request.getAno(), request.getMes(), dia))
                    .build();
            repository.save(recesso);
        });
    }

    public void removerRecessos(String id) {
        repository.deleteById(id);
    }

    public boolean ehRecesso(LocalDate data) {
        return !isEmpty(repository.findByData(data));
    }

}