package br.com.paulopinheiro.sampledb.faces.converter;

import br.com.paulopinheiro.sampledb.persistence.entity.Manufacturer;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItems;
import jakarta.faces.component.UISelectOne;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.List;
import java.util.Optional;

@FacesConverter(value="manufacturerConverter")
public class ManufacturerConverter implements Converter<Manufacturer> {

    @Override
    public Manufacturer getAsObject(FacesContext context, UIComponent component, String id) {
        if (Optional.ofNullable(id).isEmpty()) return null;

        List<Manufacturer> manufacturerList = getListFromComponent((UISelectOne) component);

        return manufacturerById(manufacturerList, Integer.valueOf(id));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Manufacturer manufacturer) {
        if (Optional.ofNullable(manufacturer).isEmpty()) return null;

        return Optional.ofNullable(manufacturer.getManufacturerId()).isPresent() ? String.valueOf(manufacturer.getManufacturerId()) : null;
    }

    private Manufacturer manufacturerById(List<Manufacturer> manufacturerList, Integer id) {
        for (Manufacturer m : manufacturerList) {
            if (m.getManufacturerId().equals(id)) return m;
        }
        return null;
    }

    private List<Manufacturer> getListFromComponent(UISelectOne selectOne) {
        UISelectItems customerSelectItems = null;

        for (UIComponent ui : selectOne.getChildren()) {
            if (ui instanceof UISelectItems uiSelectItems) {
                customerSelectItems = uiSelectItems;
                break;
            }
        }
        if (customerSelectItems == null) {
            throw new RuntimeException("Could not validate Manufacturer object");
        }

        return (List<Manufacturer>) customerSelectItems.getValue();
    }
}
