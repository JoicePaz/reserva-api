package br.ibm.reserva.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class DuracaoUtils {
	public static Integer retornaDuracao(LocalDateTime fimEm, LocalDateTime inicioEm) {
		Duration duration = Duration.between(inicioEm, fimEm);
		Long minutos = duration.getSeconds() / 60;

		return Integer.valueOf(minutos.toString()) ;
	}
}
