package com.space.service;

import com.space.model.Ship;

import java.util.Optional;


public interface ShipService {
    Iterable<Ship> getShips(ShipDTO shipDTO);

    Optional<Ship> findById(Long id);

    Integer getCount(ShipDTO shipDTO);

    Ship save(Ship ship);

    void deleteById(Long id);

    Ship update(Ship ship);
}
