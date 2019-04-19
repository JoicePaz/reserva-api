package br.ibm.reserva.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ibm.reserva.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, String> {

	List<Reserva> findByInicioEm(LocalDateTime inicioEm);
}
