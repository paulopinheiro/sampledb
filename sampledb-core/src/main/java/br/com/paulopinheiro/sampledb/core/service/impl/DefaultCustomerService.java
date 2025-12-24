package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.service.CustomerService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.CustomerDao;
import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class DefaultCustomerService implements CustomerService {
    @EJB private CustomerDao dao;

    @Override
    public List<Customer> getAllCustomers() {
        return dao.findAll();
    }

    @Override
    public void saveCustomer(Customer customer) {
        if (Optional.ofNullable(customer).isEmpty()) throw new IllegalArgumentException("Customer can't be null");

        if (Optional.ofNullable(customer.getCustomerId()).isEmpty()) dao.create(customer);
        else dao.edit(customer);
    }

    @Override
    public Customer getCustomerById(Integer customerId) {
        return dao.find(customerId);
    }

    @Override
    public void removeCustomer(Customer customer) {
        if (Optional.ofNullable(customer).isEmpty()) throw new IllegalArgumentException("Customer can't be null");

        dao.remove(customer);
    }
}
