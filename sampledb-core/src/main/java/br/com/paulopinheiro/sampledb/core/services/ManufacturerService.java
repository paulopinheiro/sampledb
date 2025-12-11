package br.com.paulopinheiro.sampledb.core.services;

import br.com.paulopinheiro.sampledb.persistence.entities.Manufacturer;
import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> getAllManufacturers();
    
    void saveManufacturer(Manufacturer manufacturer);
    Manufacturer getManufacturerById(Integer manufacturerId);
    void removeManufacturer(Manufacturer manufacturer);
}
