package com.gnosoft.api.service;

import java.util.ArrayList;
import java.util.Iterator;
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
            pecas.add(new Peca(estoque.loja(), estoque.codigo(), estoque.descricao(), estoque.quantidade(), estoque.qtdVendas(), estoque.qtdPop(), estoqueIdeal, estoque.qtd_pendente()));
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

            // Separar lojas com excesso e com falta
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
                int necessidade = Math.abs(lojaFaltando.getQtd() - lojaFaltando.getEstoqueIdeal());

                Iterator<LojasComEstoqueBalanc> iterator = lojasComExcesso.iterator();
                while (iterator.hasNext() && necessidade > 0) {
                    LojasComEstoqueBalanc lojaExcesso = iterator.next();
                    int excessoDisponivel = lojaExcesso.getQtd() - lojaExcesso.getEstoqueIdeal();

                    int quantidadeTransferencia = Math.min(necessidade, excessoDisponivel);

                    if (quantidadeTransferencia > 0) {
                        transferencias.add(new BalanceamentoTransferencia(
                            item.getCodigo(),
                            lojaExcesso.getLoja(),
                            lojaFaltando.getLoja(),
                            quantidadeTransferencia
                        ));

                        // Atualizar valores
                        lojaExcesso.setQtd(lojaExcesso.getQtd() - quantidadeTransferencia);
                        lojaFaltando.setQtd(lojaFaltando.getQtd() + quantidadeTransferencia);
                        necessidade -= quantidadeTransferencia;
                    }

                    // Se loja não tiver mais excesso, remover da lista
                    if (lojaExcesso.getQtd() <= lojaExcesso.getEstoqueIdeal()) {
                        iterator.remove();
                    }
                }

                // Se ainda restar necessidade, comprar a quantidade restante
                if (necessidade > 0) {
                    pecasComprar.add(new PecaComprar(lojaFaltando.getLoja(), item.getCodigo(), necessidade));
                    System.out.println("Comprar " + necessidade + " peças do item " + item.getCodigo() + " para a loja " + lojaFaltando.getLoja());
                }
            }
        }
        return new RespostaSuprimento(transferencias, pecasComprar);
    } 
}
