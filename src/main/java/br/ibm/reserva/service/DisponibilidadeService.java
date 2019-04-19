package br.ibm.reserva.service;

import java.time.LocalDateTime;
import java.util.List;

import br.ibm.reserva.exceptions.DisponibilidadeException;
import br.ibm.reserva.model.Disponibilidade;
import br.ibm.reserva.model.Tipo;

public interface DisponibilidadeService{
	Disponibilidade verificarDisponibilidade(Disponibilidade disponibilidade) throws DisponibilidadeException; 
	
	List<Disponibilidade> obtemDisponibilidades(LocalDateTime inicioEm, LocalDateTime fimEm, Tipo tipo);
}
