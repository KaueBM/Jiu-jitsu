package com.dev.jiujitsu.controller;

import com.dev.jiujitsu.service.FeriadoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v1/feriado")
@RestController
public class FeriadoController {

    @Autowired
    private FeriadoService feriadoService;


    @PostMapping
    @ApiOperation("Salva todos os feriados do ano")
    public ResponseEntity buscaDataGraduacoes(@RequestParam int ano) {
        feriadoService.salvarFeriados(ano);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation("Remove Feriado por id")
    public ResponseEntity removeFeriados(@RequestParam String id) {
        feriadoService.removerFeriados(id);
        return ResponseEntity.ok().build();
    }
}
