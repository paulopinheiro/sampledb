package br.com.paulopinheiro.sampledb.core.services.impl;

import br.com.paulopinheiro.sampledb.core.services.ProductCodeService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.ProductCodeDao;
import br.com.paulopinheiro.sampledb.persistence.entities.ProductCode;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class DefaultProductCodeService implements ProductCodeService {
    @EJB private ProductCodeDao dao;

    @Override
    public List<ProductCode> getAllProductCodes() {
        return dao.findAll();
    }

    @Override
    public ProductCode getProductCodeByCode(String code) {
        return dao.findProductCodeByCode(code);
    }

    @Override
    public void saveProductCode(ProductCode productCode) {
        if (Optional.ofNullable(productCode).isEmpty()) throw new IllegalArgumentException("Product code can't be null");

        if (Optional.ofNullable(productCode.getProdCode()).isEmpty()) dao.create(productCode);
        else dao.edit(productCode);
    }

    @Override
    public void removeProductCode(ProductCode productCode) {
        if (Optional.ofNullable(productCode).isEmpty()) throw new IllegalArgumentException("Product code can't be null");

        dao.remove(productCode);
    }
}
