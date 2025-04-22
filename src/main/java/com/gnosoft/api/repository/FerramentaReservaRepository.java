package com.gnosoft.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gnosoft.api.model.Ferramentaria.FerramentaReserva;

public interface FerramentaReservaRepository extends JpaRepository<FerramentaReserva, Long> {
    
}
