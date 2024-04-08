package com.dev.jiujitsu.controller;

import com.dev.jiujitsu.domain.dto.Graduacao;
import com.dev.jiujitsu.domain.request.GraduacaoRequest;
import com.dev.jiujitsu.service.GraduacaoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("v1/graduacao")
@RestController
public class GraduacaoController {

    @Autowired
    private GraduacaoService graduacaoService;

    @GetMapping
    @ApiOperation("Busca a data de graduação dos graus da faixa atual até a preta.")
    public ResponseEntity<List<Graduacao>> buscaDataGraduacoes(@RequestBody GraduacaoRequest request) {
        List<Graduacao> graduacoes = graduacaoService.buscarDatasGraduacoes(request);
        return ResponseEntity.ok(graduacoes);
    }

}