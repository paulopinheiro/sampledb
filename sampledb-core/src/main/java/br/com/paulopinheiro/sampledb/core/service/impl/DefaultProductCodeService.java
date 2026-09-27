package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.dto.ProductCodeInput;
import br.com.paulopinheiro.sampledb.core.service.DiscountCodeService;
import br.com.paulopinheiro.sampledb.core.service.ProductCodeService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.ProductCodeDao;
import br.com.paulopinheiro.sampledb.persistence.entity.ProductCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Default implementation of the ProductCode business service using pure Jakarta CDI.
 */
@ApplicationScoped
public class DefaultProductCodeService implements ProductCodeService {
    @Inject private ProductCodeDao dao;
    @Inject private DiscountCodeService discountCodeService;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<ProductCode> getAllProductCodes() {
        return dao.findAll();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public ProductCode getProductCodeByCode(String code) {
        return dao.findProductCodeByCode(code);
    }

    @Override
    @Transactional
    public void saveProductCode(ProductCodeInput input) {
        if (input==null) throw new IllegalArgumentException("Product code can't be null");
        if (input.prodCode()==null || input.prodCode().isEmpty()) throw new IllegalArgumentException("Product code must be informed");

        ProductCode existing = this.getProductCodeByCode(input.prodCode());

        if (existing==null) {  // It's a new ProductCode
            ProductCode productCode = new ProductCode(input.prodCode());
            mapInputToEntity(input,productCode);
            dao.create(productCode);
        } else {  // It's an existing Product Code
            mapInputToEntity(input,existing);
            dao.edit(existing);
        }
    }

    @Override
    @Transactional
    public void removeProductCode(String productCodeCode) {
        if (productCodeCode==null) throw new IllegalArgumentException("Product code must be informed");

        ProductCode productCode = this.getProductCodeByCode(productCodeCode);

        if (productCode!=null) dao.remove(productCode);
    }

    /**
     * Helper method to map DTO data into the JPA Entity cleanly.
     */
    private void mapInputToEntity(ProductCodeInput input, ProductCode entity) {
        entity.setDiscountCode(discountCodeService.getDiscountCodeByCode(input.discountCode()));
        entity.setDescription(input.description());
    }
}
