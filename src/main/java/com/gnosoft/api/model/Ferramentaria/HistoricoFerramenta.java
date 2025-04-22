package com.gnosoft.api.model.Ferramentaria;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.gnosoft.api.model.Colaborador;
import com.gnosoft.api.model.Filial;

@Entity
@Table(name = "movimentacao_ferramenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoricoFerramenta {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Filial filial;

    @ManyToOne(optional = false)
    private Ferramenta ferramenta;

    @Column(nullable = false)
    private int quantidade;

    @ManyToOne(optional = false)
    private Colaborador colaborador;

    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    public HistoricoFerramenta(Filial filial, Ferramenta ferramenta, int quantidade, Colaborador colaborador, TipoMovimentacao tipoMovimentacao) {
        this.filial = filial;
        this.ferramenta = ferramenta;
        this.quantidade = quantidade;
        this.colaborador = colaborador;
        this.dataMovimentacao = LocalDateTime.now();
        this.tipoMovimentacao = tipoMovimentacao;
    }
}
