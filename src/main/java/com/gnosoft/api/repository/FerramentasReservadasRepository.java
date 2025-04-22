package com.gnosoft.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gnosoft.api.model.Colaborador;
import com.gnosoft.api.model.Filial;
import com.gnosoft.api.model.Ferramentaria.ReservaFerramentaria;

@Repository
public interface FerramentasReservadasRepository extends JpaRepository<ReservaFerramentaria, Long> {

    Optional<ReservaFerramentaria> findByColaboradorAndFilial(Colaborador colaborador, Filial filial);

    List<ReservaFerramentaria> findByFilial(Filial filial);

    @Query("""
    SELECT DISTINCT r FROM com.gnosoft.api.model.Ferramentaria.ReservaFerramentaria r
    JOIN r.ferramentas fr
    WHERE fr.status = com.gnosoft.api.model.Ferramentaria.Status.EM_USO
    AND r.filial.codigo = :codigoFilial
    """)
    List<ReservaFerramentaria> findReservasComFerramentasEmUsoPorFilial(@Param("codigoFilial") String codigoFilial);

}
