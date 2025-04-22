package com.gnosoft.api.model.Ferramentaria;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservas_ferramenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FerramentaReserva { // Classe que representa uma relação muitos-para-um entre Ferramenta e ReservaFerramentaria
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Ferramenta ferramenta;

    @ManyToOne(optional = false)
    @JsonIgnore
    private ReservaFerramentaria reserva;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Integer quantidade;

    public FerramentaReserva(Ferramenta ferramenta, Integer quantidade, ReservaFerramentaria reserva, Status status) {
        this.ferramenta = ferramenta;
        this.quantidade = quantidade;
        this.reserva = reserva;
        this.status = status;
    }
}

