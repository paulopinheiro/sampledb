package br.com.paulopinheiro.sampledb.rest.resource;

import br.com.paulopinheiro.sampledb.core.dto.ProductCodeInput;
import br.com.paulopinheiro.sampledb.core.service.ProductCodeService;
import br.com.paulopinheiro.sampledb.persistence.entity.ProductCode;
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

@Path("/product-codes")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductCodeResource {
    @Inject
    private ProductCodeService service;

    @GET
    public Response getAll() {
        return Response.ok(service.getAllProductCodes()).build();
    }

    @GET
    @Path("/{code}")
    public Response getByCode(@PathParam("code") String code) {
        ProductCode pc = service.getProductCodeByCode(code);
        if (pc == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(pc).build();
    }

    @POST
    public Response save(ProductCodeInput input) {
        service.saveProductCode(input);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{code}")
    public Response remove(@PathParam("code") String code) {
        service.removeProductCode(code);
        return Response.noContent().build();
    }
}
