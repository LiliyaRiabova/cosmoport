package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.GetShipListParameters;
import com.space.service.ShipService;
import com.space.service.ShipValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
class ShipController {

    private final ShipService shipService;

    private final ShipValidatorService shipValidatorService;

    @Autowired
    public ShipController(ShipService shipService,
                          ShipValidatorService shipValidatorService) {
        this.shipService = shipService;
        this.shipValidatorService = shipValidatorService;
    }

    @RequestMapping(path = "/rest/ships", method = RequestMethod.GET)
    public List<Ship> getShipList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "order", required = false) ShipOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        GetShipListParameters parameters = new GetShipListParameters(name, planet, after, before, shipType, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        final List<Ship> ships = shipService.getShipList(parameters);
        final List<Ship> sortedShipList = shipService.sortShips(ships, order);

        return shipService.getPage(sortedShipList, pageNumber, pageSize);
    }


    @RequestMapping(path = "/rest/ships/count", method = RequestMethod.GET)
    public Integer getShipListCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating) {

        //todo Create class for parameters. like GetShipListParameters
        //todo rename service.getShips to service.getShipList
        GetShipListParameters parameters = new GetShipListParameters(name, planet, after, before, shipType, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        return shipService.getShipList(parameters).size();
    }

    @RequestMapping(path = "/rest/ships", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        if (!shipValidatorService.isShipValid(ship)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }

        ship.setSpeed(ship.getSpeed());
        final double rating = shipService.computeRating(ship.getSpeed(), ship.getUsed(), ship.getProdDate());
        ship.setRating(rating);

        final Ship savedShip = shipService.saveShip(ship);

        return new ResponseEntity<>(savedShip, HttpStatus.OK);
    }

    @RequestMapping(path = "/rest/ships/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ship> getShipById(@PathVariable(value = "id") String pathId) {
        final Long id = convertIdToLong(pathId);

        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Ship ship = shipService.getShipById(id);

        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }


    @RequestMapping(path = "/rest/ships/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Ship> updateShip(@PathVariable(value = "id") String pathId, @RequestBody Ship ship) {
        final ResponseEntity<Ship> entity = getShipById(pathId);
        final Ship savedShip = entity.getBody();
        if (savedShip == null) {
            return entity;
        }

        final Ship result;
        try {
            result = shipService.updateShip(savedShip, ship);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

      @RequestMapping(path = "/rest/ships/{id}", method = RequestMethod.DELETE)
      public ResponseEntity<Ship> deleteShip(@PathVariable(value = "id") String pathId) {
          final ResponseEntity<Ship> entity = getShipById(pathId);
          final Ship savedShip = entity.getBody();

          if (savedShip == null) {
              return entity;
          }

          shipService.deleteShip(savedShip);

          return new ResponseEntity<>(HttpStatus.OK);
      }


    private Long convertIdToLong(String pathId) {
        if (pathId == null) {
            return null;
        }

        try {
            return Long.parseLong(pathId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

