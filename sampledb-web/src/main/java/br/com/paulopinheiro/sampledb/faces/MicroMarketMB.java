package br.com.paulopinheiro.sampledb.faces;

import br.com.paulopinheiro.sampledb.core.services.MicroMarketService;
import br.com.paulopinheiro.sampledb.persistence.entities.MicroMarket;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class MicroMarketMB extends BasicMB<MicroMarket> implements Serializable {
    @EJB private MicroMarketService service;

    public MicroMarketMB() {}    

    @Override
    public boolean isNewEntity() {
        return Optional.ofNullable(getMicroMarket()).isEmpty() ||
               Optional.ofNullable(getMicroMarket().getZipCode()).isEmpty();
    }

    @Override
    protected void saveEntity(MicroMarket microMarket) {service.saveMicroMarket(microMarket);}

    @Override
    protected void deleteEntity(MicroMarket microMarket) { service.removeMicroMarket(microMarket);}

    @Override
    public List<MicroMarket> getList() {return service.getAllMicroMarkets();}

    @Override
    protected MicroMarket newEntityInstance() {
        return new MicroMarket();
    }

    public MicroMarket getMicroMarket() {return this.getEntity();}
    public void setMicroMarket(MicroMarket microMarket) {setEntity(microMarket);}
}
