package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ShipServiceImpl implements ShipService  {

    private ShipRepository shipRepository;

    private ShipValidatorService shipValidatorService;

    public ShipServiceImpl() { }

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository, ShipValidatorService shipValidatorService) {
        super();
        this.shipRepository = shipRepository;
        this.shipValidatorService = shipValidatorService;
    }

    @Override
    public Ship saveShip(Ship ship) {
        return shipRepository.save(ship);
    }

    @Override
    public Ship getShipById(Long id) {
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public Ship findAll(Long id) {
        return null;
    }

    @Override
    public Ship findById(Long id) {
        return null;
    }

    @Override
    public void deleteShip(Ship ship) {
        shipRepository.delete(ship);
    }

    @Override
    public List<Ship> getShipList(GetShipListParameters parameters) {
        final List<Ship> list = new ArrayList<>();
        shipRepository.findAll().forEach((ship) -> {
            if (parameters.getName() != null && !ship.getName().contains(parameters.getName())) return;
            if (parameters.getPlanet() != null && !ship.getPlanet().contains(parameters.getPlanet())) return;
            if (parameters.getShipType() != null && ship.getShipType() != parameters.getShipType()) return;
            if (parameters.getAfter() != null && ship.getProdDate().before(parameters.getAfter())) return;
            if (parameters.getBefore() != null && ship.getProdDate().after(parameters.getBefore())) return;
            if (parameters.getUsed() != null && ship.getUsed().booleanValue() != parameters.getUsed().booleanValue()) return;
            if (parameters.getMinSpeed() != null && ship.getSpeed().compareTo(parameters.getMinSpeed()) < 0) return;
            if (parameters.getMaxSpeed() != null && ship.getSpeed().compareTo(parameters.getMaxSpeed()) > 0) return;
            if (parameters.getMinCrewSize() != null && ship.getCrewSize().compareTo(parameters.getMinCrewSize()) < 0) return;
            if (parameters.getMaxCrewSize() != null && ship.getCrewSize().compareTo(parameters.getMaxCrewSize()) > 0) return;
            if (parameters.getMinRating() != null && ship.getRating().compareTo(parameters.getMinRating()) < 0) return;
            if (parameters.getMaxRating() != null && ship.getRating().compareTo(parameters.getMaxRating()) > 0) return;

            list.add(ship);
        });
        return list;
    }

    @Override
    public List<Ship> sortShips(List<Ship> ships, ShipOrder order) {
        if (order != null) {
            ships.sort((ship1, ship2) -> {
                switch (order) {
                    case ID: return ship1.getId().compareTo(ship2.getId());
                    case SPEED: return ship1.getSpeed().compareTo(ship2.getSpeed());
                    case DATE: return ship1.getProdDate().compareTo(ship2.getProdDate());
                    case RATING: return ship1.getRating().compareTo(ship2.getRating());
                    default: return 0;
                }
            });
        }
        return ships;
    }

    @Override
    public List<Ship> getPage(List<Ship> ships, Integer pageNumber, Integer pageSize) {
        final Integer page = pageNumber == null ? 0 : pageNumber;
        final Integer size = pageSize == null ? 3 : pageSize;
        final int from = page * size;
        int to = from + size;
        if (to > ships.size()) to = ships.size();
        return ships.subList(from, to);
    }

    @Override
    public double computeRating(double speed, boolean isUsed, Date prod) {
        final int now = 3019;
        final int prodYear = getYearFromDate(prod);
        final double k = isUsed ? 0.5 : 1;
        final double rating = 80 * speed * k / (now - prodYear + 1);
        return round(rating);
    }

    @Override
    public Ship updateShip(Ship oldShip, Ship newShip) throws IllegalArgumentException {
        boolean shouldChangeRating = false;

        final String name = newShip.getName();
        if (name != null) {
            if (shipValidatorService.isStringValid(name)) {
                oldShip.setName(name);
            } else {
                throw new IllegalArgumentException();
            }
        }

        final String planet = newShip.getPlanet();
        if (planet != null) {
            if (shipValidatorService.isStringValid(planet)) {
                oldShip.setPlanet(planet);
            } else {
                throw new IllegalArgumentException();
            }
        }

        if (newShip.getShipType() != null) {
            oldShip.setShipType(newShip.getShipType());
        }

        final Date prodDate = newShip.getProdDate();
        if (prodDate != null) {
            if (shipValidatorService.isProdDateValid(prodDate)) {
                oldShip.setProdDate(prodDate);
                shouldChangeRating = true;
            } else {
                throw new IllegalArgumentException();
            }
        }

        if (newShip.getUsed() != null) {
            oldShip.setUsed(newShip.getUsed());
            shouldChangeRating = true;
        }
        final Double speed = newShip.getSpeed();
        if (speed != null) {
            if (shipValidatorService.isSpeedValid(speed)) {
                oldShip.setSpeed(speed);
                shouldChangeRating = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        final Integer crewSize = newShip.getCrewSize();
        if (crewSize != null) {
            if (shipValidatorService.isCrewSizeValid(crewSize)) {
                oldShip.setCrewSize(crewSize);
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (shouldChangeRating) {
            final double rating = computeRating(oldShip.getSpeed(), oldShip.getUsed(), oldShip.getProdDate());
            oldShip.setRating(rating);
        }
        shipRepository.save(oldShip);

        return oldShip;
    }

    //private boolean isCrewSizeValid(Integer crewSize) {
    //        final int minCrewSize = 1;
    //        final int maxCrewSize = 9999;
    //
    //        return crewSize != null && crewSize.compareTo(minCrewSize) >= 0 && crewSize.compareTo(maxCrewSize) <= 0;
    //    }
    //
    //    private boolean isSpeedValid(Double speed) {
    //        final double minSpeed = 0.01;
    //        final double maxSpeed = 0.99;
    //        return speed != null && speed.compareTo(minSpeed) >= 0 && speed.compareTo(maxSpeed) <= 0;
    //    }

   // private boolean isStringValid(String value) {
    //        final int maxStringLength = 50;
    //        return value != null && !value.isEmpty() && value.length() <= maxStringLength;
    //    }

    //private boolean isProdDateValid(Date prodDate) {
    //        final Date startProd = getDateForYear(2800);
    //        final Date endProd = getDateForYear(3019);
    //        return prodDate != null && prodDate.after(startProd) && prodDate.before(endProd);
    //    }

   // private Date getDateForYear(int year) {
    //        final Calendar calendar = Calendar.getInstance();
    //        calendar.set(Calendar.YEAR, year);
    //        return calendar.getTime();
    //    }

    private int getYearFromDate(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    private double round(double value) {
        return Math.round(value * 100) / 100D;
    }

    @Override
    public Ship deleteById(Long id) {
        return null;
    }
}
