package br.ibm.reserva.service;

import static br.ibm.reserva.utils.DuracaoUtils.retornaDuracao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import br.ibm.reserva.exceptions.ReservaNaoEncontradaException;
import br.ibm.reserva.model.QReserva;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.ibm.reserva.exceptions.DisponibilidadeException;
import br.ibm.reserva.exceptions.DuracaoReservaException;
import br.ibm.reserva.model.Reserva;
import br.ibm.reserva.model.Status;
import br.ibm.reserva.repository.ReservaRepository;

import javax.inject.Provider;
import javax.persistence.EntityManager;

@Service
public class ReservaServiceImplementation  implements ReservaService {

	@Autowired
	private ReservaRepository reservaRepository;
	@Autowired
	private Provider<EntityManager> entityManager;

	@Override
	public Reserva criaReserva(Reserva reserva) throws DuracaoReservaException, DisponibilidadeException {
		if(verificaDisponibilidade(reserva.getInicioEm(), reserva.getFimEm())) {
			throw new DisponibilidadeException("O horário solicitado não está disponível, favor selecione um outro horário");
		}
		
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
	
	private boolean verificaDisponibilidade(LocalDateTime inicioEm, LocalDateTime fimEm) {

		for(Reserva reserva: reservaRepository.findAll()){
			if(reserva.getInicioEm().isBefore(inicioEm) && reserva.getFimEm().isAfter(fimEm)) {
				return true;
			}
			
			if(reserva.getInicioEm().isAfter(inicioEm) && reserva.getInicioEm().isBefore(fimEm)) {
				return true;
			}
			
			if(reserva.getFimEm().isAfter(inicioEm) && reserva.getFimEm().isBefore(fimEm)) {
				return true;
			}
			
			if(inicioEm.isBefore(reserva.getInicioEm()) && fimEm.isAfter(reserva.getFimEm())) {
				return true;
			}
			if(reserva.getInicioEm().isEqual(inicioEm) || reserva.getFimEm().isEqual(fimEm)) {
				return true;
			}
		}
		
		return false;
	}
	
	private float retornaValor(Integer duracao) {
		double valor = duracao * 0.5;
		
		return (float) valor;
	}
	
	private String retornaIdFormatado() {
		LocalDate data = LocalDate.now();
		
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dataFormatada = data.format(formatador);
		Random rand = new Random();
		int numero = rand.nextInt(9999);
		
		return dataFormatada + "924R1L1" + String.format("%04d", numero);		
	}

	@Override 
	public Reserva obtemReservaPorId(String idReserva) {
		Reserva reserva = reservaRepository.findById(idReserva).get();	
		return reserva;
	}

	@Override
	public List<Reserva> obtemTodasAsReservas() {

		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QReserva qReserva = QReserva.reserva;

		List<Reserva> reservas = (List<Reserva>) query.from(qReserva).
				where(qReserva.status.ne(Status.CANCELADA)).fetch();
		return reservas;
	}

	@Override
	public Reserva atualizaReserva(Reserva reserva, String idReserva) throws ReservaNaoEncontradaException {

		if(obtemReservaPorId(idReserva) == null) {
			throw new ReservaNaoEncontradaException("Reserva não encontrada");
		}

		reserva.setId(idReserva); // gambs
		Reserva reservaAtualizada = reservaRepository.save(reserva);
		return reservaAtualizada;
	}	

	@Override
	public Reserva deletaReserva(String idReserva) {
		Reserva reservaASerDeletada = reservaRepository.findById(idReserva).get();
		
		reservaASerDeletada.setCanceladaEm(LocalDateTime.now());
		reservaASerDeletada.setStatus(Status.CANCELADA);
		
		reservaRepository.save(reservaASerDeletada);
		
		return reservaASerDeletada;	
	}
}