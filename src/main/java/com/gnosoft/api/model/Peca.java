package com.gnosoft.api.model;

public class Peca {
    
    private Integer loja;
    private String codigo;
    private String descricao;
    private Integer quantidade;
    private Integer qtdVendas;
    private Integer qtdPop;
    private Integer estoqueIdeal;
    private Integer qtdPendente;

    // Contrutores

    public Peca() {
    }

    public Peca(Integer loja, String codigo, String descricao, Integer quantidade, Integer qtdVendas, Integer qtdPop, Integer estoqueIdeal, Integer qtdPendente) {
        this.loja = loja;
        this.codigo = codigo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.qtdVendas = qtdVendas;
        this.qtdPop = qtdPop;
        this.estoqueIdeal = estoqueIdeal;
        this.qtdPendente = qtdPendente;
    }

    // Getters e Setters
    public Integer getLoja() {
        return loja;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Integer getQtdVendas() {
        return qtdVendas;
    }

    public Integer getQtdPop() {
        return qtdPop;
    }

    public Integer getEstoqueIdeal() {
        return estoqueIdeal;
    }

    public Integer getQtdPendente() {
        return qtdPendente;
    }
}
