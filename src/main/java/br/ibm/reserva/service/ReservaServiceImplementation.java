package br.ibm.reserva.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ibm.reserva.model.Reserva;
import br.ibm.reserva.model.Status;
import br.ibm.reserva.repository.ReservaRepository;

@Service
public class ReservaServiceImplementation  implements ReservaService{

	@Autowired
	private ReservaRepository reservaRepository;

	@Override
	public Reserva criaReserva(Reserva reserva) {
		reserva.setStatus(Status.ATIVO);
		reserva.setCriadoEm(LocalDate.now());
		reserva.setId(retornaIdFormatado());

		reservaRepository.save(reserva);

		return reserva;
	}

	private String retornaIdFormatado() {
		LocalDate data = LocalDate.now();
		SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd");

		return formatador.format(data) + "924R1L10000";		
	}

	@Override 
	public Reserva obtemReservaPorId(String idReserva) {

		Reserva reserva = reservaRepository.findById(idReserva).get();		
		return reserva;
	}


	@Override
	public List<Reserva> obtemTodasAsReservas() {

		return reservaRepository.findAll();
	}

	@Override
	public Reserva atualizaReserva(Reserva reserva) {

		Reserva reservaAtualizada = reservaRepository.save(reserva);

		return reservaAtualizada;
	}

	@Override
	public void deletaReserva(String idReserva) {
		Reserva reservaASerDeletada = reservaRepository.findById(idReserva).get();

		reservaRepository.delete(reservaASerDeletada);

	}



}



