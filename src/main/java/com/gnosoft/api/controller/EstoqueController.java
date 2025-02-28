package com.gnosoft.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnosoft.api.model.BalanceamentoTransferencia;
import com.gnosoft.api.model.dto.CalculoEstoqueRequestDTO;
import com.gnosoft.api.service.EstoqueService;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/balancear")
    public List<BalanceamentoTransferencia> balancearEstoques(@RequestBody CalculoEstoqueRequestDTO dados) {
        System.out.println("Peças: " + dados.estoques().toString());
        return estoqueService.calcularEstoqueIdeal(dados.estoques(), dados.semanasCobertura());
    }
}
