package com.gnosoft.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gnosoft.api.model.EstoqueFerramentaria;

@Repository
public interface EstoqueFerramentaRepository extends JpaRepository<EstoqueFerramentaria, Long> {

    List<EstoqueFerramentaria> findByQuantidadeDisponivelGreaterThan(int i);

    List<EstoqueFerramentaria> findByQuantidadeReservadaGreaterThan(int i);
    
}
