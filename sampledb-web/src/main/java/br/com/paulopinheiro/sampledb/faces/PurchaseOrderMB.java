package br.com.paulopinheiro.sampledb.faces;

import br.com.paulopinheiro.sampledb.core.service.CustomerService;
import br.com.paulopinheiro.sampledb.core.service.ProductService;
import br.com.paulopinheiro.sampledb.core.service.PurchaseOrderService;
import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class PurchaseOrderMB extends BasicMB<PurchaseOrder> implements Serializable {
    @EJB private PurchaseOrderService service;
    @EJB private CustomerService customerService;
    @EJB private ProductService productService;

    public PurchaseOrderMB() {}

    public List<Customer> getCustomersList() {return customerService.getAllCustomers();}

    public List<Product> getProductsList() {return productService.getAllProducts();}

    @Override
    public boolean isNewEntity() {
        return Optional.ofNullable(getPurchaseOrder()).isEmpty() ||
               Optional.ofNullable(this.getPurchaseOrder().getOrderNum()).isEmpty();
    }

    @Override
    protected void saveEntity(PurchaseOrder purchaseOrder) {
        service.saveOrder(purchaseOrder);
    }

    @Override
    protected void deleteEntity(PurchaseOrder purchaseOrder) {
        service.removeOrder(purchaseOrder);
    }

    @Override
    public List<PurchaseOrder> getList() {
        return service.getAllOrders();
    }

    @Override
    protected PurchaseOrder newEntityInstance() {
        return new PurchaseOrder(LocalDate.now(), (short)1);
    }

    public PurchaseOrder getPurchaseOrder() {return this.getEntity();}
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {this.setEntity(purchaseOrder);}
}
