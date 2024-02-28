package com.dev.jiujitsu.controller;

import com.dev.jiujitsu.domain.dto.Graduacao;
import com.dev.jiujitsu.service.GraduacaoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v1/graduacao")
@RestController
public class GraduacaoController {

    @Autowired
    private GraduacaoService graduacaoService;


    @GetMapping
    @ApiOperation("Busca a data de graduação dos graus da faixa atual até a preta.")
    public ResponseEntity<Graduacao> buscaDataGraduacoes(@RequestParam String faixa, @RequestParam int aulasPorSemana, @RequestParam int aulasFeitas, @RequestParam int grausRecebidos) {
        Graduacao graduacao = graduacaoService.buscarDatasGraduacoes(faixa, aulasPorSemana, aulasFeitas, grausRecebidos);
        return ResponseEntity.ok(graduacao);
    }
}
