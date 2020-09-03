package com.space.controller;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ShipController {

    @Autowired
    private ShipRepository shipRepository;

    @GetMapping(path = "/rest/ships")
    public @ResponseBody Iterable<Ship> findShipsBy() {
        return shipRepository.findAll();
    }

    @GetMapping(path = "/rest/ships/{id}")
    public ResponseEntity<Ship> findShipById(@PathVariable Long id) {

        // Убираем неподходящие id
        if (id < 1)
            return ResponseEntity.badRequest().build();

        Optional<Ship> optionalResult = shipRepository.findById(id);

        // Если корабль не найден, надо вернуть 404
        if (!optionalResult.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(optionalResult.get());
    }

}
