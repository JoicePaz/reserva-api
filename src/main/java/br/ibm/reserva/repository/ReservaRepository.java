package br.ibm.reserva.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ibm.reserva.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, String> {

	// Reserva findByStatus(Status status);
}
