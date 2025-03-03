package com.gnosoft.api.model.dto;

import java.util.List;

import com.gnosoft.api.model.BalanceamentoTransferencia;

public record RespostaSuprimento(List<BalanceamentoTransferencia> transferencias, List<PecaComprar> pecasComprar) {
    
}
