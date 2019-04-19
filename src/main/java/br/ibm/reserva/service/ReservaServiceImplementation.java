package br.ibm.reserva.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ibm.reserva.exceptions.DuracaoReservaException;
import br.ibm.reserva.model.Reserva;
import br.ibm.reserva.model.Status;
import br.ibm.reserva.repository.ReservaRepository;

@Service
public class ReservaServiceImplementation  implements ReservaService{

	@Autowired
	private ReservaRepository reservaRepository;

	@Override
	public Reserva criaReserva(Reserva reserva) throws DuracaoReservaException {
		reserva.setStatus(Status.ATIVA);
		reserva.setCriadoEm(LocalDateTime.now());
		reserva.setId(retornaIdFormatado());
		reserva.setDuracao(retornaDuracao(reserva.getFimEm(), reserva.getInicioEm()));
		reserva.setValor(retornaValor(reserva.getDuracao()));

		if(reserva.getInicioEm().getMinute() != 0 || reserva.getFimEm().getMinute() != 0 || reserva.getDuracao() == 0 || reserva.getDuracao() % 60 != 0) {
			throw new DuracaoReservaException("Reservas só podem ser feitas por hora");
		}
		
		reservaRepository.save(reserva);

		return reserva;
	}
	
	private Integer retornaDuracao(LocalDateTime fimEm, LocalDateTime inicioEm) {
		Duration duration = Duration.between(inicioEm, fimEm);
		Long minutos = duration.getSeconds() / 60;
		
		return Integer.valueOf(minutos.toString()) ;
	}
	
	private float retornaValor(Integer duracao) {
		double valor = duracao * 0.5;
		
		return (float) valor;
	}
	
	private String retornaIdFormatado() {
		LocalDate data = LocalDate.now();
		
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dataFormatada = data.format(formatador);

		return dataFormatada + "924R1L10000";		
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
		
		reservaASerDeletada.setCanceladaEm(LocalDateTime.now());
		reservaASerDeletada.setStatus(Status.CANCELADA);
		
		reservaRepository.save(reservaASerDeletada);

	}



}



