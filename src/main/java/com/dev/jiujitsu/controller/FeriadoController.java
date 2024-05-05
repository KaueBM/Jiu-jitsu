package com.dev.jiujitsu.controller;

import com.dev.jiujitsu.domain.request.FeriadoRequest;
import com.dev.jiujitsu.service.FeriadoService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v1/feriado")
@RestController
public class FeriadoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeriadoController.class);

    @Autowired
    private FeriadoService feriadoService;


    @PostMapping
    @ApiOperation("Salva todos os feriados do ano")
    public ResponseEntity salvarFeriados(@RequestParam int ano) {
        LOGGER.debug("Salvando todos os feriados do ano {}", ano);
        feriadoService.salvarFeriados(ano);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation("Remove Feriado por id")
    public ResponseEntity removeFeriados(@RequestParam String id) {
        LOGGER.debug("Removendo feriado com o ID {}", id);
        feriadoService.removerFeriados(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/periodo")
    @ApiOperation("Salva o feriado por periodo")
    public ResponseEntity salvaFeriadoPorPeriodo(@RequestBody FeriadoRequest request) {
        LOGGER.debug("Salvando feriado por periodo usando os dados: {}", request);
        feriadoService.salvaFeriadoPorPeriodo(request);
        return ResponseEntity.ok().build();
    }

}
