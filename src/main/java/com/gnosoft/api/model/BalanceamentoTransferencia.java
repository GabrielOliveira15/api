package com.gnosoft.api.model;

public class BalanceamentoTransferencia {
    private String codigo;
    private Integer lojaOrigem;
    private Integer lojaDestino;
    private Integer quantidade;

    public BalanceamentoTransferencia(String codigo, Integer lojaOrigem, Integer lojaDestino, Integer quantidade) {
        this.codigo = codigo;
        this.lojaOrigem = lojaOrigem;
        this.lojaDestino = lojaDestino;
        this.quantidade = quantidade;
    }
    
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public Integer getLojaOrigem() {
        return lojaOrigem;
    }
    public Integer setLojaOrigem(Integer lojaOrigem) {
        return this.lojaOrigem = lojaOrigem;
    }
    public Integer getLojaDestino() {
        return lojaDestino;
    }
    public Integer setLojaDestino(Integer lojaDestino) {
        return this.lojaDestino = lojaDestino;
    }
    public Integer getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
