package br.com.paulopinheiro.sampledb.core.services.impl;

import br.com.paulopinheiro.sampledb.core.services.ProductService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.ProductDao;
import br.com.paulopinheiro.sampledb.persistence.entities.Product;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class DefaultProductService implements ProductService {
    @EJB private ProductDao dao;    

    @Override
    public List<Product> getAllProducts() {
        return dao.findAll();
    }

    @Override
    public void saveProduct(Product product) {
        if (Optional.ofNullable(product).isEmpty()) throw new IllegalArgumentException("Product can't be null");

        if (Optional.ofNullable(product.getProductId()).isEmpty()) dao.create(product);
        else dao.edit(product);
    }

    @Override
    public Product getProductById(Integer productId) {
        return dao.find(productId);
    }

    @Override
    public void removeProduct(Product product) {
        if (Optional.ofNullable(product).isEmpty()) throw new IllegalArgumentException("Product can't be null");

        dao.remove(product);
    }
}
