package br.ibm.reserva.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reserva")
public class Reserva {
	@Getter @Setter private Tipo tipo;
	@Getter @Setter private LocalDate inicioEm;
	@Getter @Setter private LocalDate fimEm;
	@Id
	@Getter @Setter private String id;
	@Getter @Setter private Status status;
	@Getter @Setter private LocalDate criadoEm;	
}
