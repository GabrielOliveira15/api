package com.gnosoft.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gnosoft.api.model.Filial;
import com.gnosoft.api.model.Ferramentaria.EstoqueFerramentaria;
import com.gnosoft.api.model.Ferramentaria.Ferramenta;

@Repository
public interface EstoqueFerramentaRepository extends JpaRepository<EstoqueFerramentaria, Long> {

    // Filtra por filial e quantidade disponível > 0
    List<EstoqueFerramentaria> findByFilialAndQtdDisponivelGreaterThan(Filial filial, int quantidade);

    // Busca o estoque da ferramenta por código e filial
    Optional<EstoqueFerramentaria> findByFerramentaAndFilial(Ferramenta ferramenta, Filial filial);
}
