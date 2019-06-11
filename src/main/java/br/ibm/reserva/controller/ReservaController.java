package br.ibm.reserva.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ibm.reserva.exceptions.DisponibilidadeException;
import br.ibm.reserva.exceptions.DuracaoReservaException;
import br.ibm.reserva.exceptions.StatusReservaException;
import br.ibm.reserva.model.Reserva;
import br.ibm.reserva.service.ReservaService;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

	@Autowired
	private ReservaService reservaService;
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Resource<Reserva>> reservaPost(@RequestBody @Valid Reserva reserva, UriComponentsBuilder b) throws DuracaoReservaException, DisponibilidadeException {

		Reserva reservaCriada = reservaService.criaReserva(reserva);

		UriComponents uriComponents = b.path("/api/reservas/{id}").buildAndExpand(reservaCriada.getId());

		Resource<Reserva> resource = new Resource<Reserva>(reservaCriada);
		resource.add(linkTo(methodOn(ReservaController.class)
				.reservaGet(reservaCriada.getId())).withSelfRel());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

		return new ResponseEntity<Resource<Reserva>>(resource, headers, HttpStatus.CREATED);
	}

	
	@GetMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public Reserva reservaGet(@PathVariable String idReserva) {
		
		Reserva reservaBuscada = reservaService.obtemReservaPorId(idReserva);
		return reservaBuscada;	
	}

	@GetMapping
	@ResponseStatus (HttpStatus.OK)
	@ResponseBody
	public List<Reserva> all() {
		
		List<Reserva> listaReservas;
		listaReservas = reservaService.obtemTodasAsReservas();
		
		return listaReservas;
	}
	
	@PutMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public Reserva reservaPut(@PathVariable String idReserva, @RequestBody @Valid Reserva reserva) throws StatusReservaException{
		
		Reserva reservaAtualizada =  reservaService.atualizaReserva(reserva, idReserva);
		
		return reservaAtualizada;
	}
	
	@DeleteMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public Reserva reservaDelete(@PathVariable String idReserva) {
		Reserva reservaASerDeletada = reservaService.deletaReserva(idReserva);
		
		return reservaASerDeletada;	
	}
}