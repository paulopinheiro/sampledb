package br.com.paulopinheiro.sampledb.core.services.impl;

import br.com.paulopinheiro.sampledb.core.services.ManufacturerService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.ManufacturerDao;
import br.com.paulopinheiro.sampledb.persistence.entities.Manufacturer;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class DefaultManufacturerService implements ManufacturerService {
    @EJB private ManufacturerDao dao;

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return dao.findAll();
    }

    @Override
    public void saveManufacturer(Manufacturer manufacturer) {
        if (Optional.ofNullable(manufacturer).isEmpty()) throw new IllegalArgumentException("Manufacturer can't be null");

        if (Optional.ofNullable(manufacturer.getManufacturerId()).isEmpty()) dao.create(manufacturer);
        else dao.edit(manufacturer);
    }

    @Override
    public Manufacturer getManufacturerById(Integer manufacturerId) {
        return dao.find(manufacturerId);
    }

    @Override
    public void removeManufacturer(Manufacturer manufacturer) {
        if (Optional.ofNullable(manufacturer).isEmpty()) throw new IllegalArgumentException("Manufacturer can't be null");

        dao.remove(manufacturer);
    }
}
