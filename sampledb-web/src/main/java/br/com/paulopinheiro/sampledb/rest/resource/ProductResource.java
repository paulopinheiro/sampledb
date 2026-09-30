package br.com.paulopinheiro.sampledb.rest.resource;

import br.com.paulopinheiro.sampledb.core.dto.ProductInput;
import br.com.paulopinheiro.sampledb.core.service.ProductService;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
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
 * REST Resource exposing Product catalog boundaries.
 * Enforces transactional administrative security privileges.
 */
@Path("/products")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@DeclareRoles({"admin", "operator"})
public class ProductResource {
    @Inject private ProductService service;

    /**
     * GET /api/products
     */
    @GET
    @RolesAllowed({"admin", "operator"})
    public Response getAll() {
        List<Product> products = service.getAllProducts();
        return Response.ok(products).build();
    }

    /**
     * GET /api/products/{id}
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"admin", "operator"})
    public Response getById(@PathParam("id") Integer id) {
        Product product = service.getProductById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    /**
     * POST /api/products
     */
    @POST
    @RolesAllowed("admin")
    public Response save(ProductInput input) {
        try {
            service.saveProduct(input); // Executes core pipeline + editAndRefresh alignment
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error processing product state synchronization.").build();
        }
    }

    /**
     * DELETE /api/products/{id}
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Integer id) {
        service.removeProduct(id);
        return Response.noContent().build();
    }
}
