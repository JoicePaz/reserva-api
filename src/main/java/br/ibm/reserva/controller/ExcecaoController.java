package br.ibm.reserva.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.ibm.reserva.exceptions.DisponibilidadeException;
import br.ibm.reserva.exceptions.DuracaoReservaException;

@ControllerAdvice
public class ExcecaoController{

	@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY,
			reason="Reservas só podem ser feitas por hora")
	@ExceptionHandler(DuracaoReservaException.class)
	public void valorInvalido() {

	}

	@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY,
			reason="O horário solicitado não está disponível, favor selecione um outro horário")
	@ExceptionHandler(DisponibilidadeException.class)
	public void disponibilidadeInvalida() {

	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<String> statusInvalido(InvalidFormatException ex, WebRequest request) {
		return new ResponseEntity<String>("O status da reserva só pode ser ATIVA, CANCELADA e PAGA", new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}	
	
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> reservaInvalida(NoSuchElementException ex, WebRequest request) {
		return new ResponseEntity<String>("Reserva não encontrada", new HttpHeaders(), HttpStatus.NOT_FOUND);
	}
}
