package br.com.paulopinheiro.sampledb.rest.resource;

import br.com.paulopinheiro.sampledb.core.dto.MicroMarketInput;
import br.com.paulopinheiro.sampledb.core.service.MicroMarketService;
import br.com.paulopinheiro.sampledb.persistence.entity.MicroMarket;
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
 * REST Resource exposing MicroMarket endpoints.
 * Secured with Jakarta Security roles annotations.
 */
@Path("/micro-markets")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@DeclareRoles({"admin", "operator"})
public class MicroMarketResource {
    @Inject private MicroMarketService service;

    /**
     * GET /api/micro-markets
     */
    @GET
    @RolesAllowed({"admin", "operator"})
    public Response getAll() {
        List<MicroMarket> markets = service.getAllMicroMarkets();
        return Response.ok(markets).build();
    }

    /**
     * GET /api/micro-markets/{zipCode}
     */
    @GET
    @Path("/{zipCode}")
    @RolesAllowed({"admin", "operator"})
    public Response getByZip(@PathParam("zipCode") String zipCode) {
        MicroMarket market = service.getMicroMarketByZipCode(zipCode);
        if (market == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(market).build();
    }

    /**
     * POST /api/micro-markets
     */
    @POST
    @RolesAllowed("admin")
    public Response save(MicroMarketInput input) {
        service.saveMicroMarket(input);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * DELETE /api/micro-markets/{zipCode}
     */
    @DELETE
    @Path("/{zipCode}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("zipCode") String zipCode) {
        service.removeMicroMarket(zipCode);
        return Response.noContent().build();
    }
}
