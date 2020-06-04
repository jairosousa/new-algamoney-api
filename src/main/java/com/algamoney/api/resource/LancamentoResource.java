package com.algamoney.api.resource;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Lancamento;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.LancamentoRepository;
import com.algamoney.api.repository.PessoaRepository;
import com.algamoney.api.service.PessoaService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("lancamentos")
public class LancamentoResource {

    private final LancamentoRepository lancamentoRepository;

    private final ApplicationEventPublisher publisher;

    public LancamentoResource(LancamentoRepository lancamentoRepository, ApplicationEventPublisher publisher) {
        this.lancamentoRepository = lancamentoRepository;
        this.publisher = publisher;
    }

    @GetMapping
    public List<Lancamento> listar() {
        return lancamentoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSave = lancamentoRepository.save(lancamento);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSave.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSave);

    }

    @GetMapping("/{codigo}")
    @Transactional
    public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Lancamento> optional = lancamentoRepository.findById(codigo);
        return ResponseEntity.ok(optional.get());
    }

//    @DeleteMapping("/{codigo}")
//    public ResponseEntity<?> remover(@PathVariable Long codigo) {
//        Optional<Pessoa> optional = lancamentoRepository.findById(codigo);
//        if (optional.isPresent()) {
//            lancamentoRepository.delete(optional.get());
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/{codigo}")
//    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo,
//                                            @RequestBody @Valid Pessoa pessoa) {
//        return ResponseEntity.ok(pessoaService.atualizarPessoa(codigo, pessoa));
//    }
//
//    @PutMapping("/{codigo}/ativo")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void atualizarPropriedadeAtivo(@PathVariable Long codigo,
//                                          @RequestBody Boolean ativo) {
//        pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
//    }

}
