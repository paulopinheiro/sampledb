package br.com.paulopinheiro.sampledb.faces.converter;

import br.com.paulopinheiro.sampledb.persistence.entities.ProductCode;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItems;
import jakarta.faces.component.UISelectOne;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.List;
import java.util.Optional;

@FacesConverter(value="productCodeConverter")
public class ProductCodeConverter implements Converter<ProductCode> {
    @Override
    public ProductCode getAsObject(FacesContext context, UIComponent component, String code) {
        if (Optional.ofNullable(code).isEmpty()) return null;

        List<ProductCode> productCodeList = getListFromComponent((UISelectOne) component);

        return productCodeByCode(productCodeList, code);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ProductCode productCode) {
        if (Optional.ofNullable(productCode).isEmpty()) return null;

        return Optional.ofNullable(productCode.getProdCode()).isPresent() ? String.valueOf(productCode.getProdCode()) : null;
    }

    private ProductCode productCodeByCode(List<ProductCode> productCodeList, String code) {
        for (ProductCode p: productCodeList) {
            if (p.getProdCode().equals(code)) return p;
        }
        return null;
    }

    private List<ProductCode> getListFromComponent(UISelectOne selectOne) {
        UISelectItems customerSelectItems = null;

        for (UIComponent ui : selectOne.getChildren()) {
            if (ui instanceof UISelectItems uiSelectItems) {
                customerSelectItems = uiSelectItems;
                break;
            }
        }
        if (customerSelectItems == null) {
            throw new RuntimeException("Could not validate ProductCode object");
        }

        return (List<ProductCode>) customerSelectItems.getValue();
    }
}
