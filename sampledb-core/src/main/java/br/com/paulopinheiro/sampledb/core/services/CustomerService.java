package br.com.paulopinheiro.sampledb.core.services;

import br.com.paulopinheiro.sampledb.persistence.entities.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();

    void saveCustomer(Customer customer);
    Customer getCustomerById(Integer customerId);
    void removeCustomer(Customer customer);
}
