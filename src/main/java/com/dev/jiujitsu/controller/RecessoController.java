package com.dev.jiujitsu.controller;

import com.dev.jiujitsu.domain.request.RecessoRequest;
import com.dev.jiujitsu.service.RecessoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v1/recesso")
@RestController
public class RecessoController {

        @Autowired
        private RecessoService recessoService;


        @PostMapping
        @ApiOperation("Salva todos os Recessos do ano")
        public ResponseEntity buscaDataGraduacoes(@RequestBody RecessoRequest request) {
            recessoService.salvarRecessos(request);
            return ResponseEntity.ok().build();
        }

        @DeleteMapping
        @ApiOperation("Remove Recesso por id")
        public ResponseEntity removeRecessos(@RequestParam String id) {
            recessoService.removerRecessos(id);
            return ResponseEntity.ok().build();
        }

}
