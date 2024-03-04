package com.dev.jiujitsu.controller;

import com.dev.jiujitsu.domain.dto.DiasLivres;
import com.dev.jiujitsu.domain.dto.RecessoDTO;
import com.dev.jiujitsu.service.RecessoService;
import com.dev.jiujitsu.service.util.DataUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v1/recesso")
@RestController
public class RecessosController {

    @Autowired
    private RecessoService service;


    @Autowired
    private DataUtil dataUtil;

    @PostMapping
    @ApiOperation("Cria um novo recesso")
    public ResponseEntity cadastraRecesso(@RequestBody RecessoDTO recessoDTO) {
        service.cadastrarRecesso(recessoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation("Busca máximo de dias utéis em um ano")
    public ResponseEntity<DiasLivres> buscarDiasUteis(@RequestParam int ano) {
        return ResponseEntity.ok(dataUtil.buscaMaximosDeDiasLivres(ano));
    }
}