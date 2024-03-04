package com.dev.jiujitsu.repository.impl;

import com.dev.jiujitsu.domain.entity.Feriado;
import com.dev.jiujitsu.repository.interfaces.IFeriadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Component
public class FeriadoRepositoryImpl implements IFeriadoRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public FeriadoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Feriado> buscarFeriadoPorAno(LocalDate inicioAno, LocalDate fimAno) {
        Query query = new Query(Criteria.where("data").gte(inicioAno).lte(fimAno));

        return emptyIfNull(mongoTemplate.find(query, Feriado.class));
    }
}
