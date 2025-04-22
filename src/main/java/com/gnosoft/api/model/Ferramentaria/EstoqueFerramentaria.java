package com.gnosoft.api.model.Ferramentaria;

import com.gnosoft.api.model.Filial;

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
    private Long id;

    @ManyToOne(optional = false)
    private Filial filial;

    @ManyToOne(optional = false)
    private Ferramenta ferramenta;

    @Column(nullable = false)
    private int qtdDisponivel;

    @Column(nullable = false)
    private int qtdReservada;

    @Column(nullable = true, length = 20)
    private String locacao;

    public EstoqueFerramentaria(Filial filial, Ferramenta ferramenta, int qtdDisponivel, int qtdReservada) {
        this.filial = filial;
        this.ferramenta = ferramenta;
        this.qtdDisponivel = qtdDisponivel;
        this.qtdReservada = qtdReservada;
        this.locacao = "BRANCO"; // Valor padrão para locação
    }
}