package br.com.paulopinheiro.sampledb.faces;

import br.com.paulopinheiro.sampledb.core.services.DiscountCodeService;
import br.com.paulopinheiro.sampledb.persistence.entities.DiscountCode;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class DiscountCodeMB extends BasicMB<DiscountCode> implements Serializable {
    @EJB private DiscountCodeService service;

    public DiscountCodeMB() {}    

    @Override
    public boolean isNewEntity() {
        return Optional.ofNullable(getDiscountCode()).isEmpty() || 
               Optional.ofNullable(this.getDiscountCode().getDiscountCode()).isEmpty();
    }

    @Override
    protected void saveEntity(DiscountCode entity) {
        service.saveDiscountCode(getDiscountCode());
    }

    @Override
    protected void deleteEntity(DiscountCode entity) {
        service.removeDiscountCode(getDiscountCode());
    }

    @Override
    public List<DiscountCode> getList() {
        return service.getAllDiscountCodes();
    }

    @Override
    protected DiscountCode newEntityInstance() {
        return new DiscountCode();
    }

    public DiscountCode getDiscountCode() {return getEntity();}
    public void setDiscountCode(DiscountCode discountCode) {setEntity(discountCode);}
}
