package br.ibm.reserva.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.ibm.reserva.exceptions.DisponibilidadeException;
import br.ibm.reserva.exceptions.DuracaoReservaException;

@ControllerAdvice
public class ExcecaoController {

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
	
	
}
