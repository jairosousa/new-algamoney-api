package com.algamoney.api.service;

import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa atualizarPessoa(Long codigo, Pessoa pessoa) {
        Pessoa pessoaSalva = getPessoaPorCodigo(codigo);
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
        Pessoa pessoaSalva = getPessoaPorCodigo(codigo);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    public Pessoa getPessoaPorCodigo(Long codigo) {
        return pessoaRepository.findById(codigo).get();
    }

    public Page<Pessoa> findByNomeOrAll(String nome, Pageable pageable) {
        if (nome.equalsIgnoreCase("%")) {
            return pessoaRepository.findAll(pageable);
        }

        return pessoaRepository.findByNomeContaining(nome, pageable);
    }
}
