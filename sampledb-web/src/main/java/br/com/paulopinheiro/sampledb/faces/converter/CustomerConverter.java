package br.com.paulopinheiro.sampledb.faces.converter;

import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItems;
import jakarta.faces.component.UISelectOne;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.List;
import java.util.Optional;

@FacesConverter(value="customerConverter")
public class CustomerConverter implements Converter<Customer> {

    @Override
    public Customer getAsObject(FacesContext context, UIComponent component, String id) {
        if (Optional.ofNullable(id).isEmpty()) return null;

        List<Customer> customerList = getListFromComponent((UISelectOne) component);

        return customerById(customerList, Integer.valueOf(id));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Customer customer) {
        if (Optional.ofNullable(customer).isEmpty()) return null;

        return Optional.ofNullable(customer.getCustomerId()).isPresent() ? String.valueOf(customer.getCustomerId()) : null;
    }

    private Customer customerById(List<Customer> customerList, Integer id) {
        for (Customer c : customerList) {
            if (c.getCustomerId().equals(id)) return c;
        }
        return null;
    }

    private List<Customer> getListFromComponent(UISelectOne selectOne) {
        UISelectItems customerSelectItems = null;

        for (UIComponent ui : selectOne.getChildren()) {
            if (ui instanceof UISelectItems uiSelectItems) {
                customerSelectItems = uiSelectItems;
                break;
            }
        }
        if (customerSelectItems == null) {
            throw new RuntimeException("Could not validate Customer object");
        }

        return (List<Customer>) customerSelectItems.getValue();
    }
}
