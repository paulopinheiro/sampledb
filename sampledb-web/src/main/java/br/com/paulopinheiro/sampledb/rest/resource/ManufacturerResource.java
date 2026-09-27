package br.com.paulopinheiro.sampledb.rest.resource;

import br.com.paulopinheiro.sampledb.core.dto.ManufacturerInput;
import br.com.paulopinheiro.sampledb.core.service.ManufacturerService;
import br.com.paulopinheiro.sampledb.persistence.entity.Manufacturer;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource exposing Manufacturer endpoints. Secured with Jakarta Security
 * roles annotations.
 */
@Path("/manufacturers")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@DeclareRoles({"admin", "operator"})
public class ManufacturerResource {

    @Inject
    private ManufacturerService service;

    /**
     * GET /api/manufacturers
     */
    @GET
    @RolesAllowed({"admin", "operator"})
    public Response getAll() {
        List<Manufacturer> manufacturers = service.getAllManufacturers();
        return Response.ok(manufacturers).build();
    }

    /**
     * GET /api/manufacturers/{id}
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"admin", "operator"})
    public Response getById(@PathParam("id") Integer id) {
        Manufacturer manufacturer = service.getManufacturerById(id);
        if (manufacturer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(manufacturer).build();
    }

    /**
     * POST /api/manufacturers
     */
    @POST
    @RolesAllowed("admin")
    public Response save(ManufacturerInput input) {
        try {
            service.saveManufacturer(input);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            // Devolve o HTTP 400 (Bad Request) carregando o texto exato do erro de validação
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            // Se explodiu um erro de banco (Constraint Violation), podemos mandar um aviso amigável
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro de integridade: Certifique-se de preencher Nome, Endereço, Cidade e Estado.").build();
        }
    }

    /**
     * DELETE /api/manufacturers/{id}
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Integer id) {
        service.removeManufacturer(id);
        return Response.noContent().build();
    }
}
