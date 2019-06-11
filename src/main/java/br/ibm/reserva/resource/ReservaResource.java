package br.ibm.reserva.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import br.ibm.reserva.controller.ReservaController;
import br.ibm.reserva.model.Reserva;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

@Getter
public class ReservaResource extends ResourceSupport {
    private final Reserva reserva;

    public ReservaResource(final Reserva reserva) {
        this.reserva = reserva;
        final String id = reserva.getId();

        add(linkTo(methodOn(ReservaController.class).reservaGet(id)).withSelfRel());
        add(linkTo(methodOn(ReservaController.class).all()).withRel("all"));

    }
}
