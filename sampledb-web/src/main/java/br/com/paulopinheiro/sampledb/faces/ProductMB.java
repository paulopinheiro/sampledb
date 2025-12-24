package br.com.paulopinheiro.sampledb.faces;

import br.com.paulopinheiro.sampledb.core.service.ManufacturerService;
import br.com.paulopinheiro.sampledb.core.service.ProductCodeService;
import br.com.paulopinheiro.sampledb.core.service.ProductService;
import br.com.paulopinheiro.sampledb.persistence.entity.Manufacturer;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import br.com.paulopinheiro.sampledb.persistence.entity.ProductCode;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class ProductMB extends BasicMB<Product> implements Serializable {
    @EJB private ProductService service;
    @EJB private ProductCodeService productCodeService;
    @EJB private ManufacturerService manufacturerService;

    public ProductMB() {}

    public List<ProductCode> getProductCodesList() {
        return productCodeService.getAllProductCodes();
    }

    public List<Manufacturer> getManufacturersList() {
        return manufacturerService.getAllManufacturers();
    }

    @Override
    public boolean isNewEntity() {
        return Optional.ofNullable(getProduct()).isEmpty() ||
               Optional.ofNullable(getProduct().getProductId()).isEmpty();
    }

    @Override
    protected void saveEntity(Product product) {
        service.saveProduct(product);
    }

    @Override
    protected void deleteEntity(Product product) {
        service.removeProduct(product);
    }

    @Override
    public List<Product> getList() {
        return service.getAllProducts();
    }

    @Override
    protected Product newEntityInstance() {
        Product product = new Product();
        product.setAvailable(true);
        return product;
    }

    public Product getProduct() {return this.getEntity();}
    public void setProduct(Product product) {setEntity(product);}
}
