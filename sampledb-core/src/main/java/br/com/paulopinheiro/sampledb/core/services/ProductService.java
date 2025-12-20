package br.com.paulopinheiro.sampledb.core.services;

import br.com.paulopinheiro.sampledb.persistence.entities.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    void saveProduct(Product product);
    Product getProductById(Integer productId);
    void removeProduct(Product product);
}
