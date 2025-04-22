package com.gnosoft.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gnosoft.api.model.Colaborador;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    Optional<Colaborador> findByMatricula(String matricula);
    Optional<Colaborador> findByNome(String nome);
}
