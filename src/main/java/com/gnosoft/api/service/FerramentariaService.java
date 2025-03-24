package com.gnosoft.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gnosoft.api.model.EstoqueFerramentaria;
import com.gnosoft.api.model.Ferramenta;
import com.gnosoft.api.model.MovimentacaoFerramenta;
import com.gnosoft.api.repository.EstoqueFerramentaRepository;
import com.gnosoft.api.repository.FerramentaRepository;
import com.gnosoft.api.repository.MovFerramentaRepository;

@Service
public class FerramentariaService {
    
    private final MovFerramentaRepository movFerramentaRepository;
    private final EstoqueFerramentaRepository estoqueFerramentaRepository;
    private final FerramentaRepository ferramentaRepository;

    public FerramentariaService(MovFerramentaRepository movFerramentaRepository, EstoqueFerramentaRepository estoqueFerramentaRepository, FerramentaRepository ferramentaRepository) {
        this.movFerramentaRepository = movFerramentaRepository; // Histórico de movimentações de ferramentas
        this.estoqueFerramentaRepository = estoqueFerramentaRepository; // Estoque atual da ferramenta
        this.ferramentaRepository = ferramentaRepository; // Ferramenta
    }

    // Busca ferramenta por código
    public Ferramenta buscarFerramentaPorCodigo(String codigo) {
        return ferramentaRepository.findByCodigo(codigo)
            .orElseThrow(() -> new RuntimeException("Ferramenta não encontrada!"));
    }

    // Cadastra uma nova ferramenta
    public Ferramenta cadastrarFerramenta(Ferramenta ferramenta) {
        // Verifica se a ferramenta existe
        Optional<Ferramenta> ferramentaBuscada = ferramentaRepository.findByCodigo(ferramenta.getCodigo());

        if (ferramentaBuscada.isPresent()) {
            throw new RuntimeException("Ferramenta já cadastrada!");
        } 
        return ferramentaRepository.save(ferramenta);
    }

    // Consulta o estoque de uma ferramenta
    public EstoqueFerramentaria consultarEstoqueFerramenta(String codigo) {

        // Verifica se a ferramenta existe
        Optional<Ferramenta> ferramentaBuscada = ferramentaRepository.findByCodigo(codigo);

        if (ferramentaBuscada.isEmpty()) {
            throw new RuntimeException("Ferramenta não existe");
        } 
    
        return estoqueFerramentaRepository.findById(ferramentaBuscada.get().getId()).get();
    }

    // Registra uma movimentação de ferramenta
    public String registrarMovimentacao(MovimentacaoFerramenta movimentacao) {

        String mensagem = "Nada foi feito!";

        // Verifica se a ferramenta existe
        ferramentaRepository.findById(movimentacao.getFerramenta().getId())
            .orElseThrow(() -> new RuntimeException("Ferramenta não encontrada!"));

        // Verifica se a ferramenta está no estoque
        Optional<EstoqueFerramentaria> estoqueFerramenta = estoqueFerramentaRepository.findById(movimentacao.getFerramenta().getId());

        switch (movimentacao.getTipoMovimentacao()) {
            case ENTRADA:
                estoqueFerramentaRepository.save(new EstoqueFerramentaria(movimentacao.getFerramenta(), movimentacao.getQuantidade(), 0));
                movFerramentaRepository.save(movimentacao);
                mensagem = "Entrada de " + movimentacao.getQuantidade() + " " + movimentacao.getFerramenta().getDescricao() + " registrada!";
                break;
            case RESERVA:
                estoqueFerramenta.ifPresent(estoque -> {
                    if (estoque.getQuantidadeDisponivel() < movimentacao.getQuantidade()) {
                        throw new RuntimeException("Estoque disponível insuficiente para reserva!");
                    }
                    estoque.setQuantidadeDisponivel(estoque.getQuantidadeDisponivel() - movimentacao.getQuantidade());
                    estoque.setQuantidadeReservada(estoque.getQuantidadeReservada() + movimentacao.getQuantidade());
                    estoqueFerramentaRepository.save(estoque);
                    movFerramentaRepository.save(movimentacao);
                });
                mensagem = "Reserva de " + movimentacao.getQuantidade() + " " + movimentacao.getFerramenta().getDescricao() + " registrada!";
                break;
            case DEVOLUCAO:
                estoqueFerramenta.ifPresent(estoque -> {
                    if (estoque.getQuantidadeReservada() < movimentacao.getQuantidade()) {
                        throw new RuntimeException("Estoque reservado insuficiente para devolução!");
                    }
                    estoque.setQuantidadeDisponivel(estoque.getQuantidadeDisponivel() + movimentacao.getQuantidade());
                    estoque.setQuantidadeReservada(estoque.getQuantidadeReservada() - movimentacao.getQuantidade());
                    estoqueFerramentaRepository.save(estoque);
                    movFerramentaRepository.save(movimentacao);
                });
                mensagem = "Devolução de " + movimentacao.getQuantidade() + " " + movimentacao.getFerramenta().getDescricao() + " registrada!";
                break;
            case MANUTENCAO:
                estoqueFerramenta.ifPresent(estoque -> {
                    if (estoque.getQuantidadeDisponivel() < movimentacao.getQuantidade()) {
                        throw new RuntimeException("Estoque disponível insuficiente para manutenção!");
                    }
                    estoque.setQuantidadeDisponivel(estoque.getQuantidadeDisponivel() - movimentacao.getQuantidade());
                    estoque.setQuantidadeReservada(estoque.getQuantidadeReservada() + movimentacao.getQuantidade());
                    estoqueFerramentaRepository.save(estoque);
                    movFerramentaRepository.save(movimentacao);
                });
                mensagem = "Manutenção de " + movimentacao.getQuantidade() + " " + movimentacao.getFerramenta().getDescricao() + " registrada!";
                break;
            case PERDA:
                estoqueFerramenta.ifPresent(estoque -> {
                    if (estoque.getQuantidadeDisponivel() < movimentacao.getQuantidade()) {
                        throw new RuntimeException("Estoque disponível insuficiente para perda!");
                    }
                    estoque.setQuantidadeDisponivel(estoque.getQuantidadeDisponivel() - movimentacao.getQuantidade());
                    estoqueFerramentaRepository.save(estoque);
                    movFerramentaRepository.save(movimentacao);
                });
                mensagem = "Perda de " + movimentacao.getQuantidade() + " " + movimentacao.getFerramenta().getDescricao() + " registrada!";
                break;
            default:
                break;
        }

        return mensagem;
    }

    // Consulta as ferramentas disponíveis
    public List<EstoqueFerramentaria> consultarFerramentasDisponiveis() {
        return estoqueFerramentaRepository.findByQuantidadeDisponivelGreaterThan(0);
    }

    // Consulta as ferramentas reservadas
    public List<EstoqueFerramentaria> consultarFerramentasReservadas() {
        return estoqueFerramentaRepository.findByQuantidadeReservadaGreaterThan(0);
    }
}
