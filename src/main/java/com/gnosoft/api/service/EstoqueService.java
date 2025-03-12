package com.gnosoft.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gnosoft.api.model.BalanceamentoTransferencia;
import com.gnosoft.api.model.LojasComEstoqueBalanc;
import com.gnosoft.api.model.Peca;
import com.gnosoft.api.model.dto.EstoqueLojaDTO;
import com.gnosoft.api.model.dto.PecaComprar;
import com.gnosoft.api.model.dto.RespostaSuprimento;

@Service
public class EstoqueService {

    List<Peca> pecas = new ArrayList<>();

    public RespostaSuprimento calcularEstoqueIdeal(List<EstoqueLojaDTO> estoques, Integer semanasCobertura) {
        pecas.clear(); // Limpar lista de peças
        estoques.forEach(estoque -> {
            // Estoque Ideal = quantidade de vendas do últ. 12m / 12 / 4 * semanas de cobertura;
            Integer estoqueIdeal = (int) Math.round((((double) estoque.qtdVendas() / 12) / 4) * semanasCobertura);
            pecas.add(new Peca(estoque.loja(), estoque.codigo(), estoque.descricao(), estoque.quantidade() + estoque.qtd_pendente(), estoque.qtdVendas(), estoque.qtdPop(), estoqueIdeal, estoque.qtd_pendente()));
        }); 
        return balancearEstoque();
    }

    public RespostaSuprimento balancearEstoque() {
        
        List<BalanceamentoTransferencia> transferencias = new ArrayList<>();

        List<PecaComprar> pecasComprar = new ArrayList<>();

        // Remover duplicados, pois a mesma peça pode estar em várias lojas
        List<Peca> pecasUnicas = new ArrayList<>(
            pecas.stream()
                 .collect(Collectors.toMap(Peca::getCodigo, p -> p, (p1, p2) -> p1))
                 .values()
        );

        for (Peca item : pecasUnicas) {

            // Listas criadas para separar lojas com excesso e com falta
            List<LojasComEstoqueBalanc> lojasComExcesso = new ArrayList<>();
            List<LojasComEstoqueBalanc> lojasComFalta = new ArrayList<>();

            List<LojasComEstoqueBalanc> lojas = new ArrayList<>();
            // Buscas todas as lojas que tem esse item
            pecas.stream()
                 .filter(p -> p.getCodigo().equals(item.getCodigo()))
                 .forEach(p -> lojas.add(new LojasComEstoqueBalanc(p.getLoja(), p.getCodigo(), p.getQuantidade(), p.getEstoqueIdeal())));

            // Verificar excesso e falta do item em cada loja
            lojas.forEach(loja -> {
                int diferenca = loja.getQtd() - loja.getEstoqueIdeal();

                if (diferenca > 0) {
                    lojasComExcesso.add(loja); // Excesso
                } else if (diferenca < 0) {
                    lojasComFalta.add(loja); // Falta
                }
            });
                      
        // Realizar transferências
        for (LojasComEstoqueBalanc lojaFaltando : lojasComFalta) {
            int necessidade = lojaFaltando.getEstoqueIdeal() - lojaFaltando.getQtd();

            for (int i = 0; i < lojasComExcesso.size() && necessidade > 0; ) {
                LojasComEstoqueBalanc lojaExcesso = lojasComExcesso.get(i);
                int excessoDisponivel = lojaExcesso.getQtd() - lojaExcesso.getEstoqueIdeal();

                if (excessoDisponivel > 0) {
                    int quantidadeTransferencia = Math.min(necessidade, excessoDisponivel);

                    transferencias.add(new BalanceamentoTransferencia(
                        item.getCodigo(),
                        lojaExcesso.getLoja(),
                        lojaFaltando.getLoja(),
                        quantidadeTransferencia
                    ));

                    // Atualizar os valores das lojas
                    lojaExcesso.setQtd(lojaExcesso.getQtd() - quantidadeTransferencia);
                    lojaFaltando.setQtd(lojaFaltando.getQtd() + quantidadeTransferencia);
                    necessidade -= quantidadeTransferencia;

                    // Se a loja não tiver mais excesso, remover da lista
                    if (lojaExcesso.getQtd() <= lojaExcesso.getEstoqueIdeal()) {
                        lojasComExcesso.remove(i);
                    } else {
                        i++; // Avançar o índice somente se o item não for removido
                    }
                } else {
                    i++;
                }
            }

            // Recalcular necessidade após as transferências
            int necessidadeFinal = lojaFaltando.getEstoqueIdeal() - lojaFaltando.getQtd();
            if (necessidadeFinal > 0) {
                pecasComprar.add(new PecaComprar(lojaFaltando.getLoja(), item.getCodigo(), necessidadeFinal));
                System.out.println("Comprar " + necessidadeFinal + " peças do item " + item.getCodigo() + " para a loja " + lojaFaltando.getLoja());
            }
        }
        }
        return new RespostaSuprimento(transferencias, pecasComprar);
    } 
}
