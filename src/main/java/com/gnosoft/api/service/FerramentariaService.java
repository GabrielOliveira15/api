package com.gnosoft.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gnosoft.api.model.Colaborador;
import com.gnosoft.api.model.Filial;
import com.gnosoft.api.model.Ferramentaria.EstoqueFerramentaria;
import com.gnosoft.api.model.Ferramentaria.Ferramenta;
import com.gnosoft.api.model.Ferramentaria.FerramentaReserva;
import com.gnosoft.api.model.Ferramentaria.HistoricoFerramenta;
import com.gnosoft.api.model.Ferramentaria.ReservaFerramentaria;
import com.gnosoft.api.model.Ferramentaria.Status;
import com.gnosoft.api.model.dto.RegistraMovFerramentaria;
import com.gnosoft.api.model.dto.Ferramentaria.RecebeCadastraFerramenta;
import com.gnosoft.api.repository.ColaboradorRepository;
import com.gnosoft.api.repository.EstoqueFerramentaRepository;
import com.gnosoft.api.repository.FerramentaRepository;
import com.gnosoft.api.repository.FerramentaReservaRepository;
import com.gnosoft.api.repository.FerramentasReservadasRepository;
import com.gnosoft.api.repository.FilialRepository;
import com.gnosoft.api.repository.MovFerramentaRepository;

import jakarta.transaction.Transactional;

@Service
public class FerramentariaService {
    
    // Repositórios
    private final MovFerramentaRepository movFerramentaRepository;
    private final EstoqueFerramentaRepository estoqueFerramentaRepository;
    private final FerramentaRepository ferramentaRepository;
    private final FilialRepository filialRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final FerramentasReservadasRepository ferramentasReservadasRepository;
    private final FerramentaReservaRepository ferramentaReservaRepository;

    // Construtor
    public FerramentariaService(MovFerramentaRepository movFerramentaRepository, 
                                EstoqueFerramentaRepository estoqueFerramentaRepository, 
                                FerramentaRepository ferramentaRepository, 
                                FilialRepository filialRepository,
                                ColaboradorRepository colaboradorRepository,
                                FerramentasReservadasRepository ferramentasReservadasRepository,
                                FerramentaReservaRepository ferramentaReservaRepository) {
        this.ferramentaReservaRepository = ferramentaReservaRepository; // Ferramenta reservada, tabela muitos para muitos Ferramenta e Reserva
        this.ferramentasReservadasRepository = ferramentasReservadasRepository; // Ferramentas reservadas
        this.colaboradorRepository = colaboradorRepository; // Colaborador
        this.movFerramentaRepository = movFerramentaRepository; // Histórico de movimentações de ferramentas
        this.estoqueFerramentaRepository = estoqueFerramentaRepository; // Estoque atual da ferramenta
        this.ferramentaRepository = ferramentaRepository; // Ferramenta
        this.filialRepository = filialRepository; // Filial onde a ferramenta está cadastrada
    }

    // Busca ferramenta por código OK
    public Optional<Ferramenta> buscarFerramentaPorCodigo(String codigo) {
        return ferramentaRepository.findByCodigo(codigo);
    }

    // Busca filial por código
    public Optional<Filial> buscarFilialPorCodigo(String codigo) {
        System.out.println("Filial: " + codigo);
        return filialRepository.findByCodigo(codigo);
    }

    // Busca colaborador por matrícula
    public Optional<Colaborador> buscarColaboradorPorMatricula(String matricula) {
        return colaboradorRepository.findByMatricula(matricula);
    }

    // Cadastra uma nova ferramenta OK
    public Ferramenta cadastrarFerramenta(RecebeCadastraFerramenta ferramenta) {
        // Verifica se a ferramenta existe
        Optional<Ferramenta> ferramentaBuscada = buscarFerramentaPorCodigo(ferramenta.codigo());

        if (ferramentaBuscada.isPresent()) {
            throw new RuntimeException("Ferramenta já cadastrada!");
        } 
        return ferramentaRepository.save(new Ferramenta(ferramenta.codigo(),
                                                        ferramenta.descricao()
        ));
    }

    // Consulta o estoque de uma ferramenta
    public EstoqueFerramentaria consultarEstoqueFerramenta(String codigo, String codigoFilial) {

        // Verifica se a ferramenta existe
        Optional<Ferramenta> ferramentaBuscada = ferramentaRepository.findByCodigo(codigo);

        // Verifica se a filial existe
        Optional<Filial> filialBuscada = filialRepository.findByCodigo(codigoFilial);

        // Verifica se a ferramenta e a filial existem, se sim, busca o estoque da ferramenta na filial
        // Se não achar estoque, lança exceção
        // Se a ferramenta não existe, lança exceção
        // Se a filial não existe, lança exceção
        if (ferramentaBuscada.isPresent() && filialBuscada.isPresent()) { // Verifica se a ferramenta e a filial existem
            return estoqueFerramentaRepository.findByFerramentaAndFilial(ferramentaBuscada.get(), filialBuscada.get())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado!")); 
            // Retorna o estoque da ferramenta na filial ou lança exceção se não encontrado
        } else {
            if (ferramentaBuscada.isEmpty()) {
                throw new RuntimeException("Ferramenta não encontrada!");
            } else {
                throw new RuntimeException("Filial não encontrada!");
        }
        }
    }

    // Registra uma movimentação de ferramenta
    @Transactional
    public String registrarMovimentacao(HistoricoFerramenta movimentacao) {

        Optional<EstoqueFerramentaria> estoqueFerramenta = estoqueFerramentaRepository.findByFerramentaAndFilial(movimentacao.getFerramenta(), movimentacao.getFilial());

        switch (movimentacao.getTipoMovimentacao()) {
            case ENTRADA:
                estoqueFerramentaRepository.save(new EstoqueFerramentaria(movimentacao.getFilial(), 
                    movimentacao.getFerramenta(),
                    movimentacao.getQuantidade(), 
                    0));
                movFerramentaRepository.save(movimentacao);
                break;
             
            case RESERVA:
                estoqueFerramenta.ifPresent(estoque -> {
                    if (estoque.getQtdDisponivel() < movimentacao.getQuantidade()) {
                        throw new RuntimeException("Estoque disponível insuficiente para reserva!");
                    }
                    estoque.setQtdDisponivel(estoque.getQtdDisponivel() - movimentacao.getQuantidade());
                    estoque.setQtdReservada(estoque.getQtdReservada() + movimentacao.getQuantidade());
                    estoqueFerramentaRepository.save(estoque);
                    movFerramentaRepository.save(movimentacao);
                });
                break;
            
            case DEVOLUCAO:
                estoqueFerramenta.ifPresent(estoque -> {
                    if (estoque.getQtdReservada() < movimentacao.getQuantidade()) {
                        throw new RuntimeException("Estoque reservado insuficiente para devolução!");
                    }
                    estoque.setQtdDisponivel(estoque.getQtdDisponivel() + movimentacao.getQuantidade());
                    estoque.setQtdReservada(estoque.getQtdReservada() - movimentacao.getQuantidade());
                    estoqueFerramentaRepository.save(estoque);
                    movFerramentaRepository.save(movimentacao);
                });
                break;
            case MANUTENCAO:
                estoqueFerramenta.ifPresent(estoque -> {
                    if (estoque.getQtdDisponivel() < movimentacao.getQuantidade()) {
                        throw new RuntimeException("Estoque disponível insuficiente para manutenção!");
                    }
                    estoque.setQtdDisponivel(estoque.getQtdDisponivel() - movimentacao.getQuantidade());
                    estoque.setQtdReservada(estoque.getQtdReservada() + movimentacao.getQuantidade());
                    estoqueFerramentaRepository.save(estoque);
                    movFerramentaRepository.save(movimentacao);
                });
                break;
            case PERDA:
                estoqueFerramenta.ifPresent(estoque -> {
                    if (estoque.getQtdDisponivel() < movimentacao.getQuantidade()) {
                        throw new RuntimeException("Estoque disponível insuficiente para perda!");
                    }
                    estoque.setQtdDisponivel(estoque.getQtdDisponivel() - movimentacao.getQuantidade());
                    estoqueFerramentaRepository.save(estoque);
                    movFerramentaRepository.save(movimentacao);
                });
                break;
            default:
                break;
        }
        return "mensagem";
    }

    @Transactional
    public ReservaFerramentaria registraReserva(RegistraMovFerramentaria dados) {

        ReservaFerramentaria reserva = new ReservaFerramentaria(
            this.buscarFilialPorCodigo(dados.codigoFilial()).get(),
            this.buscarColaboradorPorMatricula(dados.matriculaColaborador()).get()
        );
        ferramentasReservadasRepository.save(reserva);

        dados.ferramentas().forEach(ferr -> {
            FerramentaReserva ferramentaReserva = new FerramentaReserva(
                this.buscarFerramentaPorCodigo(ferr.codFerramenta()).get(),
                ferr.quantidade(),
                reserva,
                Status.EM_USO
            );
            ferramentaReservaRepository.save(ferramentaReserva);
        });

        return reserva;
    }

    @Transactional
    public ReservaFerramentaria registraDevolucao(RegistraMovFerramentaria dados) {

        Optional<ReservaFerramentaria> reservaBuscada = ferramentasReservadasRepository.findById(dados.idReserva());

        if (reservaBuscada.isEmpty()) {
            throw new RuntimeException("Reserva não encontrada!");
        } 

        if (reservaBuscada.isPresent()) {
            List<FerramentaReserva> ferramentasReservadas = reservaBuscada.get().getFerramentas();
            dados.ferramentas().forEach(ferr -> {
                for (FerramentaReserva ferramentaReserva : ferramentasReservadas) {
                    if (ferr.codFerramenta().equals(ferramentaReserva.getFerramenta().getCodigo())) {
                        ferramentaReserva.setStatus(Status.DEVOLVIDO);
                        ferramentaReservaRepository.save(ferramentaReserva);
                    }
                }
            });
        }

        return reservaBuscada.get();
    }

    // Consulta as ferramentas disponíveis na filial
    public List<EstoqueFerramentaria> consultarFerramentasDisponiveis(String filial_id) {
        return estoqueFerramentaRepository.findByFilialAndQtdDisponivelGreaterThan(this.buscarFilialPorCodigo(filial_id).get(),0);
    }

    // Consulta as reservas de ferramentas
    public List<ReservaFerramentaria> consultarReservasFerramentas(String filial_id) {
        return ferramentasReservadasRepository.findReservasComFerramentasEmUsoPorFilial(this.buscarFilialPorCodigo(filial_id).get().getCodigo());
    }

    public List<HistoricoFerramenta> consultarHistoricoFerramentas(String filial_id, String codFerramenta, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return movFerramentaRepository.findByFilialAndFerramentaAndDataMovimentacaoBetween(this.buscarFilialPorCodigo(filial_id).get(), 
                                                                                             this.buscarFerramentaPorCodigo(codFerramenta).get(), 
                                                                                             dataInicial, 
                                                                                             dataFinal);
    }
}
