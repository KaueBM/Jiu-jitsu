package com.dev.jiujitsu.controller;

import com.dev.jiujitsu.domain.dto.Graduacao;
import com.dev.jiujitsu.domain.request.GraduacaoRequest;
import com.dev.jiujitsu.service.GraduacaoService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/graduacao")
public class GraduacaoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraduacaoController.class);

    @Autowired
    private GraduacaoService graduacaoService;

    @GetMapping
    @ApiOperation("Busca a data de graduação dos graus da faixa atual até a preta.")
    public ResponseEntity<List<Graduacao>> buscaDataGraduacoes(@RequestBody GraduacaoRequest request) {
        LOGGER.debug("Buscando graduações com os seguintes dados: {} ", request);
        List<Graduacao> graduacoes = graduacaoService.buscarDatasGraduacoes(request);
        return ResponseEntity.ok(graduacoes);
    }

}