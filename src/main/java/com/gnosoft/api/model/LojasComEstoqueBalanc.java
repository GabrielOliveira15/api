package com.gnosoft.api.model;

public class LojasComEstoqueBalanc {
    private Integer loja;
    private String codigo;
    private Integer qtd;
    private Integer estoqueIdeal;

    public LojasComEstoqueBalanc(Integer loja, String codigo, Integer qtd, Integer estoqueIdeal) {
        this.loja = loja;
        this.codigo = codigo;
        this.qtd = qtd;
        this.estoqueIdeal = estoqueIdeal;
    }
    
    public Integer getLoja() {
        return loja;
    }
    public void setLoja(Integer loja) {
        this.loja = loja;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public Integer getQtd() {
        return qtd;
    }
    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }
    public Integer getEstoqueIdeal() {
        return estoqueIdeal;
    }
    public void setEstoqueIdeal(Integer estoqueIdeal) {
        this.estoqueIdeal = estoqueIdeal;
    }
}
