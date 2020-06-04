package com.algamoney.api.resource;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Pessoa;
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
@RequestMapping("pessoas")
public class PessoaResource {

    private final PessoaRepository pessoaRepository;

    private final ApplicationEventPublisher publisher;
    private final PessoaService pessoaService;

    public PessoaResource(PessoaRepository pessoaRepository, ApplicationEventPublisher publisher, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.publisher = publisher;
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSave = pessoaRepository.save(pessoa);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSave.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSave);

    }

    @GetMapping("/{codigo}")
    @Transactional
    public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Pessoa> optional = pessoaRepository.findById(codigo);
        return ResponseEntity.ok(optional.get());
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> remover(@PathVariable Long codigo) {
        Optional<Pessoa> optional = pessoaRepository.findById(codigo);
        if (optional.isPresent()) {
            pessoaRepository.delete(optional.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo,
                                            @RequestBody @Valid Pessoa pessoa) {

        return ResponseEntity.ok(pessoaService.atualizarPessoa(codigo, pessoa));

    }

}
