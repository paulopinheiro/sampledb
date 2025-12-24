package br.com.paulopinheiro.sampledb.core.service;

import br.com.paulopinheiro.sampledb.persistence.entity.Manufacturer;
import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> getAllManufacturers();
    
    void saveManufacturer(Manufacturer manufacturer);
    Manufacturer getManufacturerById(Integer manufacturerId);
    void removeManufacturer(Manufacturer manufacturer);
}
