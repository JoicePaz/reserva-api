package br.ibm.reserva.controller;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.ibm.reserva.exceptions.ReservaNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
import br.ibm.reserva.model.Reserva;
import br.ibm.reserva.resource.ReservaResource;
import br.ibm.reserva.service.ReservaService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
	public ResponseEntity<Resource<Reserva>> reservaPost(@RequestBody @Valid Reserva reserva, UriComponentsBuilder builder) throws DuracaoReservaException, DisponibilidadeException {

		Reserva reservaCriada = reservaService.criaReserva(reserva);

		UriComponents uriComponents = builder.path("/api/reservas/{id}").buildAndExpand(reservaCriada.getId());

		Resource<Reserva> resource = new Resource<Reserva>(reservaCriada);
		resource.add(linkTo(methodOn(ReservaController.class)
				.reservaGet(reservaCriada.getId())).withSelfRel());
		resource.add(linkTo(methodOn(ReservaController.class)
				.all()).withRel("all"));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

		return new ResponseEntity<Resource<Reserva>>(resource, headers, HttpStatus.CREATED);
	}

	
	@GetMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public Resource<Reserva> reservaGet(@PathVariable String idReserva) {
		
		Reserva reservaBuscada = reservaService.obtemReservaPorId(idReserva);

		Resource<Reserva> resource = new Resource<Reserva>(reservaBuscada);
		resource.add(linkTo(methodOn(ReservaController.class).reservaGet(reservaBuscada.getId())).withSelfRel());

		return resource;
	}

	@GetMapping
	@ResponseStatus (HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<Resources<ReservaResource>> all() {

		final List<ReservaResource> listaReservaResources = reservaService.obtemTodasAsReservas().stream().map(ReservaResource::new).collect(Collectors.toList());
		final Resources<ReservaResource> resources = new Resources<>(listaReservaResources);
		final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();

		resources.add(new Link(uriString, "self"));

		return ResponseEntity.ok(resources);
	}
	
	@PutMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public Resource<Reserva> reservaPut(@PathVariable String idReserva, @RequestBody @Valid Reserva reserva) throws ReservaNaoEncontradaException
	{
		
		Reserva reservaAtualizada =  reservaService.atualizaReserva(reserva, idReserva);

		Resource<Reserva> resource = new Resource<Reserva>(reservaAtualizada);
		resource.add(linkTo(methodOn(ReservaController.class).reservaGet(reservaAtualizada.getId())).withSelfRel());

		return resource;
	}
	
	@DeleteMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public Reserva reservaDelete(@PathVariable String idReserva) {
		Reserva reservaASerDeletada = reservaService.deletaReserva(idReserva);
		
		return reservaASerDeletada;	
	}
}