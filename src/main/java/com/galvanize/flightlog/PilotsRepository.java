package com.galvanize.flightlog;

import com.galvanize.flightlog.enities.Flights;
import com.galvanize.flightlog.enities.Pilots;
import org.springframework.data.repository.CrudRepository;

public interface PilotsRepository extends CrudRepository<Pilots, Long> {
    Pilots getById(Long id);

}
