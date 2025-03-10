package com.gnosoft.api.model.dto;

import com.gnosoft.api.model.TipoMovimentacao;

public record RegistraMovFerramentaria(String codigoFerramenta, TipoMovimentacao tipoMovimentacao, Integer quantidade, String tecnico) {
    
}
