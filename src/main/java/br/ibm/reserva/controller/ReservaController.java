package br.ibm.reserva.controller;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.xml.ws.Response;

import br.ibm.reserva.exceptions.ReservaNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
	public ResponseEntity<ReservaResource> reservaPost(@RequestBody @Valid Reserva reserva, UriComponentsBuilder builder) throws DuracaoReservaException, DisponibilidadeException {

		ReservaResource reservaResource = new ReservaResource(reservaService.criaReserva(reserva));

		UriComponents uriComponents = builder.path("/api/reservas/{id}").buildAndExpand(reservaResource.getReserva().getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());


		return new ResponseEntity<ReservaResource>(reservaResource, headers, HttpStatus.CREATED);
	}

	@GetMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public ResponseEntity<ReservaResource> reservaGet(@PathVariable String idReserva) {
		
		ReservaResource reservaBuscada = new ReservaResource(reservaService.obtemReservaPorId(idReserva));

		return new ResponseEntity<ReservaResource>(reservaBuscada, HttpStatus.OK);
	}

	@GetMapping
	@ResponseStatus (HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<Resources<ReservaResource>> all() {

		final List<ReservaResource> listaReservaResources = reservaService.obtemTodasAsReservas().stream().map(ReservaResource::new).collect(Collectors.toList());

		if(ObjectUtils.isEmpty(listaReservaResources)) {
			return ResponseEntity.notFound().build();
		}

		final Resources<ReservaResource> resources = new Resources<>(listaReservaResources);
		final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();

		resources.add(new Link(uriString, "self"));

		return ResponseEntity.ok(resources);
	}
	
	@PutMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public ResponseEntity<ReservaResource> reservaPut(@PathVariable String idReserva, @RequestBody @Valid Reserva reserva, UriComponentsBuilder builder) throws ReservaNaoEncontradaException
	{
		ReservaResource reservaResource = new ReservaResource(reservaService.atualizaReserva(reserva, idReserva));

		UriComponents uriComponents = builder.path("/api/reservas/{id}").buildAndExpand(reservaResource.getReserva().getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());

		return new ResponseEntity<ReservaResource>(reservaResource, headers, HttpStatus.OK);
	}
	
	@DeleteMapping("/{idReserva}")
	@ResponseBody
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public ResponseEntity<?> reservaDelete(@PathVariable String idReserva) throws ReservaNaoEncontradaException {

		Reserva reservaASerDeletada = reservaService.obtemReservaPorId(idReserva);

		if(reservaASerDeletada == null) {
			throw new ReservaNaoEncontradaException("");
		}

		ReservaResource reservaResource = new ReservaResource(reservaService.deletaReserva(idReserva));
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}