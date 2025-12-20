package br.com.paulopinheiro.sampledb.faces;

import br.com.paulopinheiro.sampledb.core.services.DiscountCodeService;
import br.com.paulopinheiro.sampledb.core.services.ProductCodeService;
import br.com.paulopinheiro.sampledb.persistence.entities.DiscountCode;
import br.com.paulopinheiro.sampledb.persistence.entities.ProductCode;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class ProductCodeMB extends BasicMB<ProductCode> implements Serializable {
    @EJB private ProductCodeService service;
    @EJB private DiscountCodeService discountCodeService;

    public ProductCodeMB() {}

    public List<DiscountCode> getDiscountCodeList() {
        return discountCodeService.getAllDiscountCodes();
    }

    @Override
    public boolean isNewEntity() {
        return Optional.ofNullable(getProductCode()).isEmpty() ||
               Optional.ofNullable(getProductCode().getProdCode()).isEmpty();
    }

    @Override
    protected void saveEntity(ProductCode productCode) {
        service.saveProductCode(productCode);
    }

    @Override
    protected void deleteEntity(ProductCode productCode) {
        service.removeProductCode(productCode);
    }

    @Override
    public List<ProductCode> getList() {
        return service.getAllProductCodes();
    }

    @Override
    protected ProductCode newEntityInstance() {
        return new ProductCode();
    }

    public ProductCode getProductCode() {return this.getEntity();}
    public void setProductCode(ProductCode productCode) {setEntity(productCode);}
}
