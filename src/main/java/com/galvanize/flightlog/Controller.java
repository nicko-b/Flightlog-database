package com.galvanize.flightlog;

import com.galvanize.flightlog.enities.Flights;
import com.galvanize.flightlog.enities.Pilots;
import com.galvanize.flightlog.enities.Planes;
import org.springframework.web.bind.annotation.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
public class Controller {

    private final FlightsRepository repository1;
    private final PilotsRepository repository2;
    private final PlanesRepository repository3;

    public Controller(FlightsRepository repository1, PilotsRepository repository2, PlanesRepository repository3) {
        this.repository1 = repository1;
        this.repository2 = repository2;
        this.repository3 = repository3;
    }

    @PostMapping("/addFlight")
    public Flights addFlight(@RequestBody Flights flight){
        if(flight.getDestinationLocation() == null) {
            flight.setDestinationLocation("destination location not set please add destination location");
        }
            if(flight.getDepartureLocation() == null){
                flight.setDepartureLocation("departure location not set please add departure location");
            }
        return this.repository1.save(flight);
    }

    @PatchMapping("/addLocations/{FlightId}") //edit location and datetime of flight
    public Flights addLocations(@PathVariable Long FlightId, @RequestBody Map<String, String> map){
        Flights foundFlight = this.repository1.getById(FlightId);
        if(map.containsKey("destinationLocation")){
            foundFlight.setDestinationLocation(map.get("destinationLocation"));
        }
        if(map.containsKey("departureLocation")) {
            foundFlight.setDepartureLocation(map.get("departureLocation"));
        }
        if(map.containsKey("date")){
            if(checkDateFormat(map.get("date"))) {
                foundFlight.setDate(map.get("date"));
            }
        }
        if(map.containsKey("time")){
            if(checkTimeFormat(map.get("time"))) {
                foundFlight.setTime(map.get("time"));
            }
        }
            return this.repository1.save(foundFlight);
    }

    @PostMapping("/addPilot")
    public Pilots addPilot(@RequestBody Pilots pilot){
        if(pilot.getName() == null) {
            pilot.setName("pilot name not set please add name");
        }
        return this.repository2.save(pilot);
    }

    @PatchMapping("/addPilotName/{PilotId}") //update pilot name
    public Pilots addPilotName(@PathVariable Long PilotId, @RequestBody Map<String, String> map){
        Pilots foundPilot = this.repository2.getById(PilotId);
        if(map.containsKey("name")){
            foundPilot.setName(map.get("name"));
        }
        return this.repository2.save(foundPilot);
    }

    @PostMapping("/addPlane")
    public Planes addPlane(@RequestBody Planes plane){
        if(plane.getModel() == null) {
            plane.setModel("plane model not set please add model");
        }
        return this.repository3.save(plane);
    }

    @PatchMapping("/addPlaneModel/{PlaneId}") //update plane model
    public Planes addPlaneModel(@PathVariable Long PlaneId, @RequestBody Map<String, String> map){
        Planes foundPlane = null;
        try {
                foundPlane = checkPlane(PlaneId);
            } catch (Exception e) {}
            if(map.containsKey("model")){
                foundPlane.setModel(map.get("model"));
            }
        return this.repository3.save(foundPlane);
    }

//                try {
//        checkPlane(planeI)
//
//    }      if(map.containsKey("model")){
//        foundModel.setModel(map.get("model"));
//    }
//        return this.repository3.save(foundModel);
//}

    @GetMapping("/pilotsFlights/{id}")
    public List<Flights> pilotsFlights(@PathVariable Long id) {
        return this.repository1.getByPilotId(id);
    }

    @GetMapping("/pilots")
    public Iterable<Pilots> pilots() {
        return this.repository2.findAll();
    }

    @GetMapping("/pilotsDateTime")
    public List<Flights> pilotDateTime(@RequestParam String day) {
        if(day.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return this.repository1.getByDate(day);
        }
        throw new NumberFormatException("Date format incorrect. Please use YYYY-MM-DD.");
    }

    @PatchMapping("/updatePilot/{FlightId}/{PilotId}")
    public Flights updatePilot(@PathVariable Long FlightId, @PathVariable Long PilotId) {
        Pilots foundPilot = this.repository2.getById(PilotId);
        foundPilot.setAssigned(true);
        Flights foundFlight = this.repository1.getById(FlightId);
        foundFlight.setPilotId(PilotId);
        return this.repository1.save(foundFlight);
    }

    @PatchMapping("/updateDateTime") //update dateTime of flight
    public Flights updateDateTime(@RequestBody Long FlightId, @RequestBody Map<String, String> map) {
        Flights foundFlight = this.repository1.getById(FlightId);
        if(map.containsKey("date")) {
            if(checkDateFormat(map.get("date"))) {
                foundFlight.setDate(map.get("date"));
            }
        }
            if(map.containsKey("time")) {
                if(checkDateFormat("time")) {
                    foundFlight.setTime(map.get("time"));

                }
            }
        return this.repository1.save(foundFlight);
    }

    @PatchMapping("/removePilot")
    public Flights removePilot(@RequestBody Long FlightId) {
        Flights foundFlight = this.repository1.getById(FlightId);
        foundFlight.setPilotId(null);
        Pilots foundPilot = repository2.getById(foundFlight.getPilotId());
        foundPilot.setAssigned(false);
        return this.repository1.save(foundFlight);
    }

    @PatchMapping("/updatePlaneUsedForFlight/{FlightId}/{PlaneId}")
    public Flights updatePlaneUsedForFlight(@PathVariable Long FlightId, @PathVariable Long PlaneId) {
        Flights foundFlight = this.repository1.getById(FlightId);
        foundFlight.setPlaneId(PlaneId);
        return this.repository1.save(foundFlight);
    }

    @DeleteMapping("/deletePilot/{PilotId}")
    public String deletePilot(@PathVariable Long PilotId) {
        this.repository2.deleteById(PilotId);
        return "deleted pilot" + PilotId;
    }

    @DeleteMapping("/deleteFlight/{FlightId}")
    public String deleteFlight(@PathVariable Long FlightId) {
        this.repository1.deleteById(FlightId);
        return "deleted flight" + FlightId;
    }

    @PatchMapping("/addNote")
    public String addNote(@RequestBody Long FlightId, String Notes) {
        Flights foundFlight = this.repository1.getById(FlightId);
        foundFlight.setNotes(Notes);
        this.repository1.save(foundFlight);
        return "note added to" + FlightId;
    }

    @PatchMapping("/addAvailablePilot")
    public Iterable<Flights> addAvailablePilot() {
       Iterable<Flights> flights = this.repository1.findAll();
       Iterable<Pilots> pilots = this.repository2.findAll();
        for (Flights flight : flights) {
            if (flight.getPilotId() == null) {
                for (Pilots pilot : pilots){
                    if (pilot.isAssigned() == false){
                        flight.setPilotId(pilot.getId());
                        pilot.setAssigned(true);
                    }
                    repository2.save(pilot);
                }
            }
            repository1.save(flight);
        }
        return this.repository1.findAll();
    }

    private boolean checkDateFormat(String date) {
        if(date.matches("\\d{4}-\\d{2}-\\d{2}")){
            return true;
        }
        return false;
    }

    private boolean checkTimeFormat(String time) {
        if(time.matches("\\d{2}:\\d{2}")){
            return true;
        }
        return false;
    }

    private Planes checkPlane(Long planeId) throws Exception {
        Planes foundPlane = this.repository3.getById(planeId);
        if(foundPlane != null)){
            return foundPlane;
        }
        throw new Exception("Plane not in database");
    }
}

