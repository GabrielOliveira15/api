package com.gnosoft.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gnosoft.api.model.Ferramentaria.Ferramenta;

@Repository
public interface FerramentaRepository extends JpaRepository<Ferramenta, Long> {

    // Método para buscar uma ferramenta pelo código
    public Optional<Ferramenta> findByCodigo(String codigo);
}
