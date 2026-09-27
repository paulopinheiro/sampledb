package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.dto.CustomerInput;
import br.com.paulopinheiro.sampledb.core.service.CustomerService;
import br.com.paulopinheiro.sampledb.core.service.DiscountCodeService;
import br.com.paulopinheiro.sampledb.core.service.MicroMarketService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.CustomerDao;
import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Default implementation of the Customer business service using pure Jakarta CDI.
 */
@ApplicationScoped
public class DefaultCustomerService implements CustomerService {
    @Inject private CustomerDao dao;
    @Inject private DiscountCodeService discountCodeService;
    @Inject private MicroMarketService microMarketService;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Customer> getAllCustomers() {
        return dao.findAll();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Customer getCustomerById(Integer customerId) {
        return dao.find(customerId);
    }

    @Override
    @Transactional
    public void saveCustomer(CustomerInput input) {
        if (input==null) throw new IllegalArgumentException("Customer input cannot be null");
        /* Primary key is not automatic */
        if (input.customerId()==null) throw new IllegalArgumentException("Customer id must be informed");

        Customer existing = this.getCustomerById(input.customerId());

        if (existing==null) { //It's a new customer, with the Id informed
            Customer customer = new Customer(input.customerId());
            mapInputToEntity(input,customer);
            dao.create(customer);
        } else { //It's an existing customer
            mapInputToEntity(input,existing);
            dao.edit(existing);
        }
    }

    @Override
    @Transactional
    public void removeCustomer(Integer customerId) {
        if (customerId==null) throw new IllegalArgumentException("Customer Id must be informed");

        Customer customer = this.getCustomerById(customerId);

        if (customer!=null) dao.remove(customer);
    }

    /**
     * Helper method to map DTO data into the JPA Entity cleanly.
     */
    private void mapInputToEntity(CustomerInput input, Customer entity) {
        entity.setName(input.name());
        entity.setAddressLine1(input.addressLine1());
        entity.setAddressLine2(input.addressLine2());
        entity.setCity(input.city());
        entity.setState(input.state());
        entity.setPhone(input.phone());
        entity.setFax(input.fax());
        entity.setEmail(input.email());
        entity.setCreditLimit(input.creditLimit());
        entity.setDiscountCode(discountCodeService.getDiscountCodeByCode(input.discountCode()));
        entity.setMicroMarket(microMarketService.getMicroMarketByZipCode(input.zipCode()));
    }
}
