package com.gnosoft.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gnosoft.api.model.Filial;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
    
    Optional<Filial> findByCodigo(String codigo); // Busca filial por código
}
