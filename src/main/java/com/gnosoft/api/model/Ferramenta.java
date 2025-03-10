package com.gnosoft.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "ferramenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ferramenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo", unique = true)
    private String codigo;

    @Column(name = "descricao")
    private String descricao;

    public Ferramenta(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
