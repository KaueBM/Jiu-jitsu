package com.dev.jiujitsu.repository.interfaces;

import com.dev.jiujitsu.domain.entity.Recesso;

import java.time.LocalDate;
import java.util.List;

public interface IRecessoRepository {

    List<Recesso> buscarRecessoPorAno(LocalDate startDate, LocalDate endDate);

}