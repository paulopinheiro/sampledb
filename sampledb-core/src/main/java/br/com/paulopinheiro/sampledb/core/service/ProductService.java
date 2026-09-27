package br.com.paulopinheiro.sampledb.core.service;

import br.com.paulopinheiro.sampledb.core.dto.ProductInput;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    public void subtractFromProductQuantity(Product product, Integer quantityTaken);
    void saveProduct(ProductInput input);
    Product getProductById(Integer productId);
    void removeProduct(Integer productId);
}
