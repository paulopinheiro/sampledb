package br.com.paulopinheiro.sampledb.rest.resource;

import br.com.paulopinheiro.sampledb.core.dto.CustomerInput;
import br.com.paulopinheiro.sampledb.core.service.CustomerService;
import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
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
 * REST Resource exposing Customer management boundaries.
 * Liberated for both Admin and Operator workflow roles.
 */
@Path("/customers")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@DeclareRoles({"admin", "operator"})
public class CustomerResource {
    @Inject private CustomerService service;

    /**
     * GET /api/customers
     * Open to all corporate profiles.
     */
    @GET
    @RolesAllowed({"admin", "operator"})
    public Response getAll() {
        List<Customer> customers = service.getAllCustomers();
        return Response.ok(customers).build();
    }

    /**
     * GET /api/customers/{id}
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"admin", "operator"})
    public Response getById(@PathParam("id") Integer id) {
        Customer customer = service.getCustomerById(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(customer).build();
    }

    /**
     * POST /api/customers
     * Operador e Admin POSSUEM direito de cadastrar e atualizar clientes.
     */
    @POST
    @RolesAllowed({"admin", "operator"})
    public Response save(CustomerInput input) {
        try {
            service.saveCustomer(input);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error processing customer persistence allocation.").build();
        }
    }

    /**
     * DELETE /api/customers/{id}
     * Por segurança do negócio, a remoção física de uma conta continua Admin-Only.
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Integer id) {
        service.removeCustomer(id);
        return Response.noContent().build();
    }
}