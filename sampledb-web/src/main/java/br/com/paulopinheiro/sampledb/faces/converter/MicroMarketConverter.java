package br.com.paulopinheiro.sampledb.faces.converter;

import br.com.paulopinheiro.sampledb.persistence.entities.MicroMarket;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItems;
import jakarta.faces.component.UISelectOne;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.List;
import java.util.Optional;

@FacesConverter(value="microMarketConverter")
public class MicroMarketConverter implements Converter<MicroMarket>{

    @Override
    public MicroMarket getAsObject(FacesContext context, UIComponent component, String zipCode) {
        if (Optional.ofNullable(zipCode).isEmpty()) return null;

        List<MicroMarket> microMarketsList = getListFromComponent((UISelectOne) component);

        return microMarketById(microMarketsList, zipCode);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, MicroMarket microMarket) {
        if (Optional.ofNullable(microMarket).isEmpty()) return null;

        return Optional.ofNullable(microMarket.getZipCode()).isPresent() ? String.valueOf(microMarket.getZipCode()) : null;
    }

     private List<MicroMarket> getListFromComponent(UISelectOne selectOne) {
        UISelectItems customerSelectItems = null;

        for (UIComponent ui : selectOne.getChildren()) {
            if (ui instanceof UISelectItems uiSelectItems) {
                customerSelectItems = uiSelectItems;
                break;
            }
        }
        if (customerSelectItems == null) {
            throw new RuntimeException("Could not validate MicroMarket object");
        }

        return (List<MicroMarket>) customerSelectItems.getValue();
    }    

    private MicroMarket microMarketById(List<MicroMarket> microMarketsList, String zipCode) {
        for (MicroMarket m : microMarketsList) {
            if (m.getZipCode().equals(zipCode)) return m;
        }
        return null;
    }
}
