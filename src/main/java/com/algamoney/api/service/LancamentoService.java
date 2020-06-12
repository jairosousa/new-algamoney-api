package com.algamoney.api.service;

import com.algamoney.api.model.Lancamento;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.LancamentoRepository;
import com.algamoney.api.repository.PessoaRepository;
import com.algamoney.api.service.exception.LancamentoInexistenteException;
import com.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final PessoaRepository pessoaRepository;

    public LancamentoService(LancamentoRepository lancamentoRepository, PessoaRepository pessoaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public Lancamento salvar(Lancamento lancamento) {
        getPessoaByCodigo(lancamento.getPessoa().getCodigo());
        return lancamentoRepository.save(lancamento);
    }

    public void remover(Long codigo) {
        Lancamento lancamento = getLancamentoByCodigo(codigo);
        lancamentoRepository.delete(lancamento);
    }

    private Lancamento getLancamentoByCodigo(Long codigo) {
        Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
        if (!lancamento.isPresent()) {
            throw new LancamentoInexistenteException();
        }

        return lancamento.get();
    }

    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

        return lancamentoRepository.save(lancamentoSalvo);
    }

    private Optional<Pessoa> getPessoaByCodigo(Long codigo) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);

        if (!pessoa.isPresent() || pessoa.get().isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        return pessoa;
    }

    private void validarPessoa(Lancamento lancamento) {
        Optional<Pessoa> pessoa = Optional.empty();
        if (lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
        }

        if (!pessoa.isPresent() || pessoa.get().isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamentoExistente(Long codigo) {
        Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);
        if (!lancamentoSalvo.isPresent()) {
            throw new IllegalArgumentException();
        }
        return lancamentoSalvo.get();
    }
}
