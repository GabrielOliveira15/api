package com.gnosoft.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gnosoft.api.model.Filial;
import com.gnosoft.api.model.Ferramentaria.Ferramenta;
import com.gnosoft.api.model.Ferramentaria.HistoricoFerramenta;

@Repository
public interface MovFerramentaRepository extends JpaRepository<HistoricoFerramenta, Long> {

    List<HistoricoFerramenta> findByFilialAndFerramenta(Filial filial, Ferramenta ferramenta);

    List<HistoricoFerramenta> findByFilialAndFerramentaAndDataMovimentacaoBetween(Filial filial, 
                                                                                  Ferramenta ferramenta,
                                                                                  LocalDateTime dataInicial, 
                                                                                  LocalDateTime dataFinal);
    
}
