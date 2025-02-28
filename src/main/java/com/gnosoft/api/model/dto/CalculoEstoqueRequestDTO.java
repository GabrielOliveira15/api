package com.gnosoft.api.model.dto;

import java.util.List;

public record CalculoEstoqueRequestDTO(List<EstoqueLojaDTO> estoques, Integer semanasCobertura) {
    
}
