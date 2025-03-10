package com.gnosoft.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estoque_ferramentaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstoqueFerramentaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Ferramenta ferramenta;

    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel;

    @Column(name = "quantidade_reservada", nullable = false)
    private Integer quantidadeReservada;

    public EstoqueFerramentaria(Ferramenta ferramenta, Integer quantidadeDisponivel, Integer quantidadeReservada) {
        this.ferramenta = ferramenta;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.quantidadeReservada = quantidadeReservada;
    }
}
