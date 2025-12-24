package br.com.paulopinheiro.sampledb.faces;

import br.com.paulopinheiro.sampledb.core.service.CustomerService;
import br.com.paulopinheiro.sampledb.core.service.DiscountCodeService;
import br.com.paulopinheiro.sampledb.core.service.MicroMarketService;
import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import br.com.paulopinheiro.sampledb.persistence.entity.DiscountCode;
import br.com.paulopinheiro.sampledb.persistence.entity.MicroMarket;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class CustomerMB extends BasicMB<Customer> implements Serializable {
    @EJB private CustomerService service;
    @EJB private MicroMarketService microMarketService;
    @EJB private DiscountCodeService discountCodeService;

    public CustomerMB() {}

    public List<MicroMarket> getMicroMarkets() {return microMarketService.getAllMicroMarkets();}
    public List<DiscountCode> getDiscountCodes() {return discountCodeService.getAllDiscountCodes();}

    @Override
    public boolean isNewEntity() {
        return Optional.ofNullable(getCustomer()).isEmpty() ||
               Optional.ofNullable(this.getCustomer().getCustomerId()).isEmpty();
    }

    @Override
    protected void saveEntity(Customer customer) {
        service.saveCustomer(customer);
    }

    @Override
    protected void deleteEntity(Customer customer) {
        service.removeCustomer(customer);
    }

    @Override
    public List<Customer> getList() {
        return service.getAllCustomers();
    }

    @Override
    protected Customer newEntityInstance() {
        return new Customer();
    }

    public Customer getCustomer() {return this.getEntity();}
    public void setCustomer(Customer customer) {this.setEntity(customer);}
}
