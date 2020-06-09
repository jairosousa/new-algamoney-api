package com.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.algamoney.api.event.RecursoCriadoEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.algamoney.api.model.Categoria;
import com.algamoney.api.repository.CategoriaRepository;

@RestController
@RequestMapping("categorias")
public class CategoriaResource {

	private final CategoriaRepository categoriaRepository;

	private final ApplicationEventPublisher publisher;

	public CategoriaResource(CategoriaRepository categoriaRepository, ApplicationEventPublisher publisher) {
		this.categoriaRepository = categoriaRepository;
		this.publisher = publisher;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSave = categoriaRepository.save(categoria);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSave.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSave);
		
	}
	
	@GetMapping("/{codigo}")
	@Transactional
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Categoria> optional = categoriaRepository.findById(codigo);
		return optional.isPresent() ? ResponseEntity.ok(optional.get())
				: ResponseEntity.notFound().build();
	}

}
