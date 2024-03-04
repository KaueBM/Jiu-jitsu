package com.dev.jiujitsu.repository;

import com.dev.jiujitsu.domain.entity.Feriado;
import com.dev.jiujitsu.repository.interfaces.IFeriadoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeriadoRepository extends MongoRepository<Feriado, String>, IFeriadoRepository {

    List<Feriado> findByData(LocalDate date);

    Feriado findTopByOrderByDataDesc();

}