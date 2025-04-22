package com.gnosoft.api.model.dto;

import java.util.List;

import com.gnosoft.api.model.Ferramentaria.TipoMovimentacao;

public record RegistraMovFerramentaria(String codigoFilial, 
                                       TipoMovimentacao tipoMovimentacao, 
                                       String matriculaColaborador, 
                                       List<RecebeFerramentaQuantidadeReserva> ferramentas,
                                       Long idReserva) {
    
}
