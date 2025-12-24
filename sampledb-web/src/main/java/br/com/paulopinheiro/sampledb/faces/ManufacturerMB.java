package br.com.paulopinheiro.sampledb.faces;

import br.com.paulopinheiro.sampledb.core.service.ManufacturerService;
import br.com.paulopinheiro.sampledb.persistence.entity.Manufacturer;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class ManufacturerMB implements Serializable {
    private static final String PAGE_TITLE="Manufacturers Registration";
    @EJB private ManufacturerService service;
    
    private Manufacturer manufacturer;

    public ManufacturerMB() {}

    public void save(ActionEvent evt) {
        try {
            service.saveManufacturer(getManufacturer());
            clear(evt);
            successMessage("Manufacturer saved successfully.");
        } catch (Exception ex) {
            errorMessage(ex.getMessage());
        }
    }

    public void clear(ActionEvent evt) {
        setManufacturer(new Manufacturer());
    }

    public void remove(ActionEvent evt) {
        try {
            service.removeManufacturer(getManufacturer());
            clear(evt);
            successMessage("Manufacturer removed successfully.");
        } catch (Exception ex) {
            errorMessage(ex.getMessage());
        }
    }

    public boolean isNewRegistration() {
        return Optional.ofNullable(this.getManufacturer().getManufacturerId()).isEmpty();
    }

    public Manufacturer getManufacturer() {
        if (Optional.ofNullable(this.manufacturer).isEmpty())
            this.manufacturer = new Manufacturer();
        return manufacturer;
    }
    public void setManufacturer(Manufacturer manufacturer) {this.manufacturer = manufacturer;}

    public List<Manufacturer> getManufacturersList() {
        return service.getAllManufacturers();
    }

    public String getPageTitle() {
        return PAGE_TITLE;
    }
    private void errorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,message,null));
    }

    private void successMessage(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,mensagem,null));
    }
}
