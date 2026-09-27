package br.com.paulopinheiro.sampledb.core.service;

import br.com.paulopinheiro.sampledb.core.dto.CustomerInput;
import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(Integer customerId);

    void saveCustomer(CustomerInput input);
    void removeCustomer(Integer customerId);
}
