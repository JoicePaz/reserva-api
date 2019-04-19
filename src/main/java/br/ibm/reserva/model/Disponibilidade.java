package br.ibm.reserva.model;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class Disponibilidade {
	@NonNull @Getter @Setter private Tipo tipo;
	@NonNull @Getter @Setter private LocalDateTime inicioEm;
	@NonNull @Getter @Setter private LocalDateTime fimEm;
	@Getter @Setter private Integer duracao;
	
	private Integer retornaDuracao(LocalDateTime fimEm, LocalDateTime inicioEm) {
		Duration duration = Duration.between(inicioEm, fimEm);
		Long minutos = duration.getSeconds() / 60;

		return Integer.valueOf(minutos.toString()) ;
	}

	public Disponibilidade(@NonNull Tipo tipo, @NonNull LocalDateTime inicioEm, @NonNull LocalDateTime fimEm) {
		super();
		this.tipo = tipo;
		this.inicioEm = inicioEm;
		this.fimEm = fimEm;
		this.duracao = retornaDuracao(fimEm, inicioEm);
	}
}
