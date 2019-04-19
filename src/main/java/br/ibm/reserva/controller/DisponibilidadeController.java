package br.ibm.reserva.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ibm.reserva.exceptions.DisponibilidadeException;
import br.ibm.reserva.model.Disponibilidade;
import br.ibm.reserva.model.Tipo;
import br.ibm.reserva.service.DisponibilidadeService;

@RestController
@RequestMapping("/api/disponibilidade")
public class DisponibilidadeController {

	@Autowired
	private DisponibilidadeService disponibilidadeService;
	
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Disponibilidade disponibilidadePost(@RequestBody @Valid Disponibilidade disponibilidade) throws DisponibilidadeException {
		
		Disponibilidade disponibilidadeVerificada = disponibilidadeService.verificarDisponibilidade(disponibilidade);
		
		return disponibilidadeVerificada;
		
	}
	
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<Disponibilidade> disponibilidadesGet(@RequestParam String inicioEm, @RequestParam String fimEm, @RequestParam Tipo tipo) throws DisponibilidadeException {
		
		List<Disponibilidade> disponibilidades = disponibilidadeService.obtemDisponibilidades(LocalDateTime.parse(inicioEm), LocalDateTime.parse(fimEm), tipo);
		
		return disponibilidades;
		
	}
	
}
