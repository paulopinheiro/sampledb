package br.com.paulopinheiro.sampledb.faces.converter;

import br.com.paulopinheiro.sampledb.persistence.entities.DiscountCode;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItems;
import jakarta.faces.component.UISelectOne;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.List;
import java.util.Optional;

@FacesConverter(value="discountCodeConverter")
public class DiscountCodeConverter implements Converter<DiscountCode> {    

    @Override
    public DiscountCode getAsObject(FacesContext context, UIComponent component, String code) {
        if (Optional.ofNullable(code).isEmpty()) return null;

        List<DiscountCode> discountCodeList = getListFromComponent((UISelectOne) component);

        return discountCodeByCode(discountCodeList, code);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, DiscountCode discountCode) {
        if (Optional.ofNullable(discountCode).isEmpty()) return null;

        return Optional.ofNullable(discountCode.getDiscountCode()).isPresent() ? String.valueOf(discountCode.getDiscountCode()) : null;
    }

    private DiscountCode discountCodeByCode(List<DiscountCode> discountCodeList, String code) {
        for (DiscountCode d : discountCodeList) {
            if (d.getDiscountCode().equals(code)) return d;
        }
        return null;
    }

    private List<DiscountCode> getListFromComponent(UISelectOne selectOne) {
        UISelectItems customerSelectItems = null;

        for (UIComponent ui : selectOne.getChildren()) {
            if (ui instanceof UISelectItems uiSelectItems) {
                customerSelectItems = uiSelectItems;
                break;
            }
        }
        if (customerSelectItems == null) {
            throw new RuntimeException("Could not validate DiscountCode object");
        }

        return (List<DiscountCode>) customerSelectItems.getValue();
    }
}
