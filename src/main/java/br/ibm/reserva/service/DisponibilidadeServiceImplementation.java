package br.ibm.reserva.service;

import static br.ibm.reserva.utils.DuracaoUtils.retornaDuracao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ibm.reserva.exceptions.DisponibilidadeException;
import br.ibm.reserva.model.Disponibilidade;
import br.ibm.reserva.model.Reserva;
import br.ibm.reserva.model.Tipo;
import br.ibm.reserva.repository.ReservaRepository;

@Service
public class DisponibilidadeServiceImplementation implements DisponibilidadeService{
	@Autowired
	private ReservaRepository reservaRepository;

	@Override
	public Disponibilidade verificarDisponibilidade(Disponibilidade disponibilidade) throws DisponibilidadeException {
		disponibilidade.setDuracao(retornaDuracao(disponibilidade.getFimEm(), disponibilidade.getInicioEm()));
		
		for(Reserva reserva: reservaRepository.findAll()){
			if(reserva.getInicioEm().isBefore(disponibilidade.getInicioEm()) && reserva.getFimEm().isAfter(disponibilidade.getFimEm()) && reserva.getTipo() == disponibilidade.getTipo()) {
				throw new DisponibilidadeException("");
			}

			if(reserva.getInicioEm().isAfter(disponibilidade.getInicioEm()) && reserva.getInicioEm().isBefore(disponibilidade.getFimEm()) && reserva.getTipo() == disponibilidade.getTipo()) {
				throw new DisponibilidadeException("");
			}

			if(reserva.getFimEm().isAfter(disponibilidade.getInicioEm()) && reserva.getFimEm().isBefore(disponibilidade.getFimEm()) && reserva.getTipo() == disponibilidade.getTipo()) {
				throw new DisponibilidadeException("");
			}

			if(disponibilidade.getInicioEm().isBefore(reserva.getInicioEm()) && disponibilidade.getFimEm().isAfter(reserva.getFimEm()) && reserva.getTipo() == disponibilidade.getTipo()) {
				throw new DisponibilidadeException("");
			}
			if((reserva.getInicioEm().isEqual(disponibilidade.getInicioEm()) || reserva.getFimEm().isEqual(disponibilidade.getFimEm())) && reserva.getTipo() == disponibilidade.getTipo()) {
				throw new DisponibilidadeException("");
			}
		}

		return disponibilidade;

	}

	@Override
	public List<Disponibilidade> obtemDisponibilidades(LocalDateTime inicioEm, LocalDateTime fimEm, Tipo tipo) {
		List<Disponibilidade> disponibilidades = geraListaHorariosDiponiveis(inicioEm, fimEm, tipo);
		
		List<Disponibilidade> disp = new ArrayList<Disponibilidade>();
		
		for(Disponibilidade disponibilidade: disponibilidades){
			for(Reserva reserva: reservaRepository.findAll()) {
				if(!(disponibilidade.getInicioEm().equals(reserva.getInicioEm()) && disponibilidade.getFimEm().equals(reserva.getFimEm()) && disponibilidade.getTipo().equals(reserva.getTipo()))) {
					disp.add(disponibilidade);
				}
			}
			
		}
		return disp;
	}
	
	private List<Disponibilidade> geraListaHorariosDiponiveis(LocalDateTime inicioEm, LocalDateTime fimEm, Tipo tipo) {
		List<Disponibilidade> disponibilidades = new ArrayList<Disponibilidade>();
		
		if(tipo == Tipo.HARD) {
			disponibilidades.add(new Disponibilidade(Tipo.SAIBRO, inicioEm, fimEm));
			disponibilidades.add(new Disponibilidade(Tipo.HARD, inicioEm.minusMinutes(60), fimEm.minusMinutes(60)));
			disponibilidades.add(new Disponibilidade(Tipo.HARD, inicioEm.plusMinutes(60), fimEm.plusMinutes(60)));
			disponibilidades.add(new Disponibilidade(Tipo.SAIBRO, inicioEm.minusMinutes(60), fimEm.minusMinutes(60)));
			disponibilidades.add(new Disponibilidade(Tipo.SAIBRO, inicioEm.plusMinutes(60), fimEm.plusMinutes(60)));
			disponibilidades.add(new Disponibilidade(Tipo.HARD, inicioEm.minusMinutes(120), fimEm.minusMinutes(120)));
			disponibilidades.add(new Disponibilidade(Tipo.HARD, inicioEm.plusMinutes(120), fimEm.plusMinutes(120)));
		}
		else {
			disponibilidades.add(new Disponibilidade(Tipo.HARD, inicioEm, fimEm));
			disponibilidades.add(new Disponibilidade(Tipo.SAIBRO, inicioEm.minusMinutes(60), fimEm.minusMinutes(60)));
			disponibilidades.add(new Disponibilidade(Tipo.SAIBRO, inicioEm.plusMinutes(60), fimEm.plusMinutes(60)));
			disponibilidades.add(new Disponibilidade(Tipo.HARD, inicioEm.minusMinutes(60), fimEm.minusMinutes(60)));
			disponibilidades.add(new Disponibilidade(Tipo.HARD, inicioEm.plusMinutes(60), fimEm.plusMinutes(60)));
			disponibilidades.add(new Disponibilidade(Tipo.SAIBRO, inicioEm.minusMinutes(120), fimEm.minusMinutes(120)));
			disponibilidades.add(new Disponibilidade(Tipo.SAIBRO, inicioEm.plusMinutes(120), fimEm.plusMinutes(120)));
		}
		
		return disponibilidades;
	}
	
}
