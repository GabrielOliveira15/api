package com.gnosoft.api.model.Ferramentaria;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ferramentas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ferramenta {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String descricao;

    public Ferramenta(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
