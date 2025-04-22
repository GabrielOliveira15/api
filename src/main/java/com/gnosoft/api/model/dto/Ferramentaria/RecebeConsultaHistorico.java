package com.gnosoft.api.model.dto.Ferramentaria;

import java.sql.Date;

public record RecebeConsultaHistorico(String filial_id, String codFerramenta, Date dataInicial, Date dataFinal) {
    
}
