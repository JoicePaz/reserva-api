package br.ibm.reserva.service;

import java.util.List;

import br.ibm.reserva.exceptions.DisponibilidadeException;
import br.ibm.reserva.exceptions.DuracaoReservaException;
import br.ibm.reserva.exceptions.ReservaNaoEncontradaException;
import br.ibm.reserva.model.Reserva;

public interface ReservaService {
	Reserva criaReserva(Reserva reserva) throws DuracaoReservaException, DisponibilidadeException;
	
	Reserva obtemReservaPorId(String idReserva);
	
	List<Reserva> obtemTodasAsReservas();
	
	Reserva atualizaReserva(Reserva reserva, String idReserva) throws ReservaNaoEncontradaException;
	
	Reserva deletaReserva(String idReserva) throws ReservaNaoEncontradaException;
	
}
