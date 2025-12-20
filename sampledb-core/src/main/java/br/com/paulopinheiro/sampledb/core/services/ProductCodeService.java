package br.com.paulopinheiro.sampledb.core.services;

import br.com.paulopinheiro.sampledb.persistence.entities.ProductCode;
import java.util.List;

public interface ProductCodeService {
    List<ProductCode> getAllProductCodes();
    ProductCode getProductCodeByCode(String code);

    void saveProductCode(ProductCode productCode);
    void removeProductCode(ProductCode productCode);
}
