package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.dto.ManufacturerInput;
import br.com.paulopinheiro.sampledb.core.service.ManufacturerService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.ManufacturerDao;
import br.com.paulopinheiro.sampledb.persistence.entity.Manufacturer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Year;
import java.util.List;

/**
 * Default implementation of the Manufacturer business service using pure Jakarta CDI.
 */
@ApplicationScoped 
public class DefaultManufacturerService implements ManufacturerService {

    @Inject
    private ManufacturerDao dao;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Manufacturer> getAllManufacturers() {
        return dao.findAll();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Manufacturer getManufacturerById(Integer manufacturerId) {
        return dao.find(manufacturerId);
    }

    @Override
    @Transactional
    public void saveManufacturer(ManufacturerInput input) {
        if (input == null) throw new IllegalArgumentException("Manufacturer input cannot be null");

        /* The Manufacturer Id is a sequence per year, so if the Id is not informed
         * we assume it's a new manufacturer and give it the next available id
         */
        if (input.manufacturerId() == null) {
            Manufacturer newManufacturer = new Manufacturer(getNextId());
            mapInputToEntity(input, newManufacturer);

            dao.create(newManufacturer);
        } else {
            Manufacturer existing = this.getManufacturerById(input.manufacturerId());
            if (existing != null) {
                mapInputToEntity(input, existing);
                dao.edit(existing);
            }
        }
    }

    @Override
    @Transactional
    public void removeManufacturer(Integer manufacturerId) {
        if (manufacturerId==null) throw new IllegalArgumentException("Manufacturer Id must be informed");

        Manufacturer manufacturer = this.getManufacturerById(manufacturerId);

        if (manufacturer!=null) dao.remove(manufacturer);
    }

    /**
     * Helper method to map DTO data into the JPA Entity cleanly.
     */
    private void mapInputToEntity(ManufacturerInput input, Manufacturer entity) {
        entity.setName(input.name());
        entity.setAddressLine1(input.addressLine1());
        entity.setAddressLine2(input.addressLine2());
        entity.setCity(input.city());
        entity.setState(input.state());
        entity.setZip(input.zip());
        entity.setPhone(input.phone());
        entity.setFax(input.fax());
        entity.setEmail(input.email());
        entity.setRep(input.rep());
    }

    /* The manufacturer ID is a sequence per year. */
    private int getNextId() {
        int currentYear = Year.now().getValue();
        int idPrefix = currentYear * 10000;

        Integer maxIdForYear = dao.findMaxIdByYearPrefix(idPrefix);
        return (maxIdForYear == null || maxIdForYear == 0) 
                    ? idPrefix + 1 
                    : maxIdForYear + 1;
    }
}
