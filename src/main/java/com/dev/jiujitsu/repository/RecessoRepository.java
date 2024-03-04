package com.dev.jiujitsu.repository;

import com.dev.jiujitsu.domain.entity.Recesso;
import com.dev.jiujitsu.repository.interfaces.IRecessoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecessoRepository extends MongoRepository<Recesso, String>, IRecessoRepository {

    List<Recesso> findByData(LocalDate date);

}