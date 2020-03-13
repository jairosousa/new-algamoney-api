package com.algamoney.api.exceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe vai capturar exceções
 * 
 * @author jnasciso
 *
 */
@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource source;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String mensagemUsuário = source.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());

		String mensagemDesenvolvedor = ex.getCause().toString();

		return handleExceptionInternal(ex, new Erro(mensagemUsuário, mensagemDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request);
	}

	public static class Erro {

		private String mensagemUsuário;
		private String mensagemDesenvolvedor;

		public Erro(String mensagemUsuário, String mensagemDesenvolvedor) {
			super();
			this.mensagemUsuário = mensagemUsuário;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}

		public String getMensagemUsuário() {
			return mensagemUsuário;
		}

		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}

	}

}
