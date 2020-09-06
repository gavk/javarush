package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipDTO;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ShipController {

    @Autowired
    private ShipService shipService;

    @GetMapping(path = "/rest/ships")
    public ResponseEntity<Iterable<Ship>> findShips(String name, String planet, ShipType shipType, Long after,
                                                    Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                                    Integer minCrewSize, Integer maxCrewSize, Double minRating,
                                                    Double maxRating, Integer pageNumber, Integer pageSize,
                                                    ShipOrder order) {

        ShipDTO shipDTO = new ShipDTO(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating, pageNumber, pageSize, order);

        Iterable<Ship> result = shipService.getShips(shipDTO);
        return ResponseEntity.ok(result);
    }


    @GetMapping(path = "/rest/ships/{id}")
    public ResponseEntity<Ship> findShipById(@PathVariable Long id) {

        // Убираем неподходящие id
        if (id < 1)
            return ResponseEntity.badRequest().build();

        Optional<Ship> optionalResult = shipService.findById(id);

        // Если корабль не найден, надо вернуть 404
        if (!optionalResult.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(optionalResult.get());
    }

    @GetMapping(path = "/rest/ships/count")
    public ResponseEntity<Integer> getCount(String name, String planet, ShipType shipType, Long after,
                                            Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                            Integer minCrewSize, Integer maxCrewSize, Double minRating,
                                            Double maxRating, Integer pageNumber, Integer pageSize,
                                            ShipOrder order) {
        ShipDTO shipDTO = new ShipDTO(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating, pageNumber, pageSize, order);
        return ResponseEntity.ok(shipService.getCount(shipDTO));
    }


    @PostMapping(path = "/rest/ships")
    public ResponseEntity<Ship> save(@RequestBody Ship ship) {

        if (ship.validate())
            return ResponseEntity.ok(shipService.save(ship));
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(path = "/rest/ships/{id}")
    public ResponseEntity<Ship> deleteById(@PathVariable Long id) {
        if (id < 1)
            return ResponseEntity.badRequest().build();
        Optional<Ship> ship = shipService.findById(id);
        if (!ship.isPresent())
            return ResponseEntity.notFound().build();
        shipService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/rest/ships/{id}")
    public ResponseEntity<Ship> update(@PathVariable Long id, @RequestBody Ship ship) {

        if (id < 1)
            return ResponseEntity.badRequest().build();

        if (!ship.isValidForUpdate())
            return ResponseEntity.badRequest().build();

        Optional<Ship> optionalFoundShip = shipService.findById(id);
        if (optionalFoundShip.isPresent()) {
            Ship foundShip = optionalFoundShip.get();
            foundShip.update(ship);
            return ResponseEntity.ok(shipService.update(foundShip));
        }

        return ResponseEntity.notFound().build();
    }
}
