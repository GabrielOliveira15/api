package com.gnosoft.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "movimentacao_ferramenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovimentacaoFerramenta {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ferramenta_id", nullable = false)
    private Ferramenta ferramenta;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipoMovimentacao;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private String tecnico;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataMovimentacao = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("America/Sao_Paulo"));

    public MovimentacaoFerramenta(Ferramenta ferramenta, TipoMovimentacao tipoMovimentacao, Integer quantidade, String tecnico) {
        this.ferramenta = ferramenta;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
        this.tecnico = tecnico;
    }

}
