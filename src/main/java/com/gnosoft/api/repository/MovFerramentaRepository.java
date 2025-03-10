package com.gnosoft.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gnosoft.api.model.MovimentacaoFerramenta;

@Repository
public interface MovFerramentaRepository extends JpaRepository<MovimentacaoFerramenta, Long> {
    
}
