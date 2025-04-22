package com.gnosoft.api.model.Ferramentaria;

import java.time.LocalDateTime;
import java.util.List;

import com.gnosoft.api.model.Colaborador;
import com.gnosoft.api.model.Filial;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservas_ferramentaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservaFerramentaria {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Filial filial;

    @ManyToOne(optional = false)
    private Colaborador colaborador;

    @Column(nullable = false)
    private LocalDateTime dataReserva;

    @OneToMany(mappedBy = "reserva")
    private List<FerramentaReserva> ferramentas;

    public ReservaFerramentaria(Filial filial, Colaborador colaborador) {
        this.filial = filial;
        this.colaborador = colaborador;
        this.dataReserva = LocalDateTime.now();
    }

}
