package br.com.paulopinheiro.sampledb.faces.converter;

import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItems;
import jakarta.faces.component.UISelectOne;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.List;
import java.util.Optional;

@FacesConverter(value="productConverter")
public class ProductConverter implements Converter<Product> {

    @Override
    public Product getAsObject(FacesContext context, UIComponent component, String id) {
        if (Optional.ofNullable(id).isEmpty()) return null;

        List<Product> productList = getListFromComponent((UISelectOne) component);

        return productById(productList, Integer.valueOf(id));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Product product) {
        if (Optional.ofNullable(product).isEmpty()) return null;

        return Optional.ofNullable(product.getProductId()).isPresent() ? String.valueOf(product.getProductId()) : null;
    }

    private Product productById(List<Product> productList, Integer id) {
        for (Product p : productList) {
            if (p.getProductId().equals(id)) return p;
        }
        return null;
    }

    private List<Product> getListFromComponent(UISelectOne selectOne) {
        UISelectItems customerSelectItems = null;

        for (UIComponent ui : selectOne.getChildren()) {
            if (ui instanceof UISelectItems uiSelectItems) {
                customerSelectItems = uiSelectItems;
                break;
            }
        }
        if (customerSelectItems == null) {
            throw new RuntimeException("Could not validate Product object");
        }

        return (List<Product>) customerSelectItems.getValue();
    }
}
