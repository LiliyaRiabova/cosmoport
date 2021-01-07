package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ShipService {
    Ship saveShip(Ship ship);

    Ship getShipById(Long id);

    Ship findAll(Long id);

    Ship findById(Long id);

    Ship deleteById (Long id);

    Ship updateShip(Ship oldShip, Ship newShip) throws IllegalArgumentException;

    void deleteShip(Ship ship);

    List<Ship> getShipList(GetShipListParameters parameters);

    List<Ship> sortShips(List<Ship> ships, ShipOrder order);

    List<Ship> getPage(List<Ship> ships, Integer pageNumber, Integer pageSize);

    double computeRating(double speed, boolean isUsed, Date prod);
}

