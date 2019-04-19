package br.ibm.reserva.model;

import static br.ibm.reserva.utils.DuracaoUtils.retornaDuracao;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
public class Disponibilidade {
	@NonNull @Getter @Setter private Tipo tipo;
	@NonNull @Getter @Setter private LocalDateTime inicioEm;
	@NonNull @Getter @Setter private LocalDateTime fimEm;
	@Getter @Setter private Integer duracao;
	

	public Disponibilidade(@NonNull Tipo tipo, @NonNull LocalDateTime inicioEm, @NonNull LocalDateTime fimEm) {
		super();
		this.tipo = tipo;
		this.inicioEm = inicioEm;
		this.fimEm = fimEm;
		this.duracao = retornaDuracao(fimEm, inicioEm);
	}
}
