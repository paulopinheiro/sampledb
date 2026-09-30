package br.com.paulopinheiro.sampledb.rest.resource;

import br.com.paulopinheiro.sampledb.core.dto.PurchaseOrderInput;
import br.com.paulopinheiro.sampledb.core.service.PurchaseOrderService;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
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

@Path("/purchase-orders")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@DeclareRoles({"admin", "operator"})
public class PurchaseOrderResource {
    @Inject private PurchaseOrderService service;

    @GET
    @RolesAllowed({"admin", "operator"})
    public Response getAll() {
        List<PurchaseOrder> orders = service.getAllOrders();
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderNum}")
    @RolesAllowed({"admin", "operator"})
    public Response getByNum(@PathParam("orderNum") Integer orderNum) {
        PurchaseOrder order = service.getOrderByNum(orderNum);
        if (order == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(order).build();
    }

    @POST
    @RolesAllowed({"admin", "operator"})
    public Response save(PurchaseOrderInput input) {
        try {
            service.saveOrder(input); // Dispara as validações e deduções do core
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Erro ao processar alocação financeira da ordem.").build();
        }
    }

    @DELETE
    @Path("/{orderNum}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("orderNum") Integer orderNum) {
        service.removeOrder(orderNum);
        return Response.noContent().build();
    }
}
