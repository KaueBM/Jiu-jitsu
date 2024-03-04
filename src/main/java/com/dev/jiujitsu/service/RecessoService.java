package com.dev.jiujitsu.service;

import com.dev.jiujitsu.converter.RecessoConverter;
import com.dev.jiujitsu.domain.dto.RecessoDTO;
import com.dev.jiujitsu.domain.entity.Recesso;
import com.dev.jiujitsu.repository.RecessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class RecessoService {

    @Autowired
    private RecessoConverter converter;

    @Autowired
    private RecessoRepository repository;

    public void cadastrarRecesso(RecessoDTO recessoDTO) {
        if (Objects.isNull(recessoDTO) || isEmpty(recessoDTO.getDias()))
            throw new IllegalArgumentException("Os dias do recesso devem ser informados.");


        recessoDTO.getDias().forEach(dia -> {
            Recesso recesso = converter.toDomain(recessoDTO, dia);
            repository.save(recesso);
        });
    }

    public boolean ehRecesso(LocalDate data) {
        return !isEmpty(repository.findByData(data));
    }

}