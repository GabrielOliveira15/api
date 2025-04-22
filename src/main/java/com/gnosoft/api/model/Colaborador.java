package com.gnosoft.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "colaboradores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Colaborador {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(optional = false)
    private Filial filial;
}
