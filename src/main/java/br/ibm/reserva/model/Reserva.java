package br.ibm.reserva.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reserva")
public class Reserva {
	@Getter @Setter private Tipo tipo;
	@Getter @Setter private LocalDateTime inicioEm;
	@Getter @Setter private LocalDateTime fimEm;
	@Id
	@Getter @Setter private String id;
	@Getter @Setter private Status status;
	@Getter @Setter private LocalDateTime criadoEm;	
	@Getter @Setter private Integer duracao;
	@Getter @Setter private Float valor;
}
