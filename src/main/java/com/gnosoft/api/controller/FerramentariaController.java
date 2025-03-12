package com.gnosoft.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnosoft.api.model.EstoqueFerramentaria;
import com.gnosoft.api.model.Ferramenta;
import com.gnosoft.api.model.MovimentacaoFerramenta;
import com.gnosoft.api.model.dto.CadastraFerramenta;
import com.gnosoft.api.model.dto.RegistraMovFerramentaria;
import com.gnosoft.api.service.FerramentariaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/ferramentaria")
public class FerramentariaController {
    
    private FerramentariaService ferramentariaService;

    public FerramentariaController(FerramentariaService ferramentariaService) {
        this.ferramentariaService = ferramentariaService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Ferramenta> cadastraFerramenta(@RequestBody CadastraFerramenta ferramenta) {
        return ResponseEntity.ok(ferramentariaService.cadastrarFerramenta(new Ferramenta(ferramenta.codigo(), ferramenta.descricao())));
    }

    @PostMapping("/movimentar")
    public ResponseEntity<String> movimentarFerramentaria(@RequestBody RegistraMovFerramentaria dados) {
        return ResponseEntity.ok(ferramentariaService.registrarMovimentacao(new MovimentacaoFerramenta(
            ferramentariaService.buscarFerramentaPorCodigo(dados.codigoFerramenta()), 
            dados.tipoMovimentacao(),
            dados.quantidade(),
            dados.tecnico()
        )));
    }
    
    @GetMapping("consultar/{codigo}")
    public ResponseEntity<EstoqueFerramentaria> consultarEstoqueFerramenta(@PathVariable String codigo) {
        return ResponseEntity.ok(ferramentariaService.consultarEstoqueFerramenta(codigo));
    }
    
}
