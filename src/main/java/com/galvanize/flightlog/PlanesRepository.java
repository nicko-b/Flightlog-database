package com.galvanize.flightlog;

import com.galvanize.flightlog.enities.Flights;
import com.galvanize.flightlog.enities.Pilots;
import com.galvanize.flightlog.enities.Planes;
import org.springframework.data.repository.CrudRepository;

public interface PlanesRepository extends CrudRepository<Planes, Long> {
    Planes getById(Long id);
}
