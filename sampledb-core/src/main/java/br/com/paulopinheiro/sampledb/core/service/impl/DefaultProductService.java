package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.dto.ProductInput;
import br.com.paulopinheiro.sampledb.core.service.ManufacturerService;
import br.com.paulopinheiro.sampledb.core.service.ProductCodeService;
import br.com.paulopinheiro.sampledb.core.service.ProductService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.ProductDao;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class DefaultProductService implements ProductService {

    @Inject private ProductDao dao;    
    @Inject private ManufacturerService manufacturerService;    
    @Inject private ProductCodeService productCodeService;    

    @Override
    @Transactional
    public void subtractFromProductQuantity(Product product, Integer quantityTaken) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");

        // Note: quantityTaken can be negative here if we are RESTORING stock on order removal!
        int newQuantity = product.getQuantityOnHand() - quantityTaken;

        if (newQuantity < 0) {
            throw new IllegalArgumentException("There are only " + product.getQuantityOnHand() 
                    + " units available of " + product.getDescription());
        }

        product.setQuantityOnHand(newQuantity);
        
        // Employs the new DAO strategy to guarantee consistency with the Database Trigger
        dao.editAndRefresh(product);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Product> getAllProducts() {
        return dao.findAll();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Product getProductById(Integer productId) {
        return dao.find(productId);
    }

    @Override
    @Transactional
    public void saveProduct(ProductInput input) {
        if (input == null) throw new IllegalArgumentException("Product input cannot be null");
        if (input.productId()==null) throw new IllegalArgumentException("Product Id must be informed");

        Product existing = this.getProductById(input.productId());

        if (existing == null) { //It's a new Product
            Product product = new Product(input.productId());
            mapInputToEntity(input,product);
            dao.create(product);
        } else { // It's an existing product
            mapInputToEntity(input, existing);
            dao.editAndRefresh(existing); // Consistent write
        }
    }

    @Override
    @Transactional
    public void removeProduct(Integer productId) {
        if (productId==null) throw new IllegalArgumentException("Product Id must be informed");

        Product product = this.getProductById(productId);

        if (product!=null) dao.remove(product);
    }

    private void mapInputToEntity(ProductInput input, Product entity) {
        entity.setPurchaseCost(input.purchaseCost());
        entity.setQuantityOnHand(input.quantityOnHand());
        entity.setMarkup(input.markup());
        entity.setAvailable(input.available());
        entity.setDescription(input.description());
        entity.setManufacturer(manufacturerService.getManufacturerById(input.manufacturerId()));
        entity.setProductCode(productCodeService.getProductCodeByCode(input.prodCode()));
    }
}
