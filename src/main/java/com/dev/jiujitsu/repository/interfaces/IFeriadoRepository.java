package com.dev.jiujitsu.repository.interfaces;

import com.dev.jiujitsu.domain.entity.Feriado;

import java.time.LocalDate;
import java.util.List;


public interface IFeriadoRepository {

    List<Feriado> buscarFeriadoPorAno(LocalDate startDate, LocalDate endDate);

}