package com.gnosoft.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnosoft.api.model.Filial;
import com.gnosoft.api.model.Ferramentaria.EstoqueFerramentaria;
import com.gnosoft.api.model.Ferramentaria.Ferramenta;
import com.gnosoft.api.model.Ferramentaria.HistoricoFerramenta;
import com.gnosoft.api.model.Ferramentaria.ReservaFerramentaria;
import com.gnosoft.api.model.Ferramentaria.TipoMovimentacao;
import com.gnosoft.api.model.dto.RegistraMovFerramentaria;
import com.gnosoft.api.model.dto.Ferramentaria.RecebeCadastraFerramenta;
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
    public ResponseEntity<Ferramenta> cadastraFerramenta(@RequestBody RecebeCadastraFerramenta ferramenta) {
        return ResponseEntity.ok(ferramentariaService.cadastrarFerramenta(ferramenta));
    }

    @GetMapping("consultar/{codigoFilial}/{codigo}")
    public ResponseEntity<EstoqueFerramentaria> consultarEstoqueFerramenta(@PathVariable String codigoFilial, @PathVariable String codigo) {
        return ResponseEntity.ok(ferramentariaService.consultarEstoqueFerramenta(codigo, codigoFilial));
    }

    @PostMapping("/movimentar")
    public ResponseEntity<String> movimentarFerramentaria(@RequestBody RegistraMovFerramentaria dados) {

        System.out.println("Dados recebidos: " + dados.toString());

        // Verifica se a filial existe
        Optional<Filial> filialBuscada = ferramentariaService.buscarFilialPorCodigo(dados.codigoFilial());
        if (filialBuscada.isEmpty()) {
            return ResponseEntity.badRequest().body("Filial não encontrada!");
        }

        dados.ferramentas().forEach(mov -> {
            ferramentariaService.registrarMovimentacao(new HistoricoFerramenta(
                filialBuscada.get(),
                ferramentariaService.buscarFerramentaPorCodigo(mov.codFerramenta()).orElseThrow(() -> new RuntimeException("Ferramenta não encontrada!")),
                mov.quantidade(),
                ferramentariaService.buscarColaboradorPorMatricula(dados.matriculaColaborador()).orElseThrow(() -> new RuntimeException("Colaborador não encontrado!")),
                dados.tipoMovimentacao()
            ));
        });

        // Cria uma reserva se a movimentação for de reserva ou manutenção
        if (dados.tipoMovimentacao() == TipoMovimentacao.RESERVA || dados.tipoMovimentacao() == TipoMovimentacao.MANUTENCAO) {
            ferramentariaService.registraReserva(dados);
        }

        if (dados.tipoMovimentacao() == TipoMovimentacao.DEVOLUCAO) {
            ferramentariaService.registraDevolucao(dados);
        }

        return ResponseEntity.ok("Movimentação realizada com sucesso!");
    }

    @GetMapping("disponiveis/{filial_id}")
    public ResponseEntity<List<EstoqueFerramentaria>> consultarFerramentasDisponiveis(@PathVariable String filial_id) {
        return ResponseEntity.ok(ferramentariaService.consultarFerramentasDisponiveis(filial_id));
    }

    @GetMapping("reservadas/{filial_id}")
    public ResponseEntity<List<ReservaFerramentaria>> consultarFerramentasReservadas(@PathVariable String filial_id) {
        return ResponseEntity.ok(ferramentariaService.consultarReservasFerramentas(filial_id));
    }

    @GetMapping("historico/{filial_id}/{codFerramenta}/{dataInicial}/{dataFinal}")
    public ResponseEntity<List<HistoricoFerramenta>> consultarHistoricoFerramenta(@PathVariable String filial_id, 
                                                                                  @PathVariable String codFerramenta, 
                                                                                  @PathVariable LocalDateTime dataInicial,
                                                                                  @PathVariable LocalDateTime dataFinal) {
        return ResponseEntity.ok(ferramentariaService.consultarHistoricoFerramentas(filial_id, codFerramenta, dataInicial, dataFinal));
    }
    
}
