package br.com.paulopinheiro.sampledb.rest.resource;

import br.com.paulopinheiro.sampledb.core.dto.DiscountCodeInput;
import br.com.paulopinheiro.sampledb.core.service.DiscountCodeService;
import br.com.paulopinheiro.sampledb.persistence.entity.DiscountCode;
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
 * REST Resource exposing DiscountCode endpoints.
 * Operates purely with JSON data format and utilizes JAX-RS annotations.
 */

@Path("/discount-codes") // Base URL: http://localhost:8080/sampledb/api/discount-codes
@RequestScoped // REST resources should be short-lived (created per HTTP request)
@Produces(MediaType.APPLICATION_JSON) // Automatically converts Java returns to JSON
@Consumes(MediaType.APPLICATION_JSON) // Expects JSON payload in request bodies
@DeclareRoles({"admin", "operator"})
public class DiscountCodeResource {
    @Inject private DiscountCodeService service; // Service-to-Resource boundary (DIP)

    /**
     * GET method to list all discount codes.
     * Endpoint: GET /api/discount-codes
     */
    @GET
    @RolesAllowed({"admin", "operator"})
    public Response getAll() {
        List<DiscountCode> codes = service.getAllDiscountCodes();
        return Response.ok(codes).build(); // HTTP 200 OK with JSON array
    }

    /**
     * GET method to find a specific code by its path variable.
     * Endpoint: GET /api/discount-codes/{code}
     */
    @GET
    @Path("/{code}")
    @RolesAllowed({"admin", "operator"})
    public Response getByCode(@PathParam("code") String code) {
        DiscountCode discountCode = service.getDiscountCodeByCode(code);
        if (discountCode == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // HTTP 404 Not Found
        }
        return Response.ok(discountCode).build(); // HTTP 200 OK
    }

    /**
     * POST method to create or update a discount code using our safe Record DTO.
     * Endpoint: POST /api/discount-codes
     */
    @POST
    @RolesAllowed("admin")
    public Response save(DiscountCodeInput input) {
        service.saveDiscountCode(input);
        // Returns HTTP 201 Created for a seamless REST architecture
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * DELETE method to remove a code.
     * Endpoint: DELETE /api/discount-codes/{code}
     */
    @DELETE
    @Path("/{code}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("code") String code) {
        service.removeDiscountCode(code);
        return Response.noContent().build(); // HTTP 204 No Content (Standard successful delete)
    }
}
