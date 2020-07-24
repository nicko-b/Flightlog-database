package com.galvanize.flightlog;

import com.galvanize.flightlog.enities.Flights;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface FlightsRepository extends CrudRepository<Flights, Long> {
    List<Flights> getByPilotId(Long id);
    List<Flights> getByDate(String date);
    Flights getById(Long id);

}
