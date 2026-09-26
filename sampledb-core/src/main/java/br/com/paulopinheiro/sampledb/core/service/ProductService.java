package br.com.paulopinheiro.sampledb.core.service;

import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    public void subtractFromProductQuantity(Product product, Integer quantityTaken);
    void saveProduct(Product product);
    Product getProductById(Integer productId);
    void removeProduct(Product product);
}
