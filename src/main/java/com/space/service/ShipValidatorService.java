package com.space.service;

import com.space.model.Ship;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Calendar;

@Service
public class ShipValidatorService {

    final int start = 2800;
    final int now = 3019;
    final int minCrewSize = 1;
    final int maxCrewSize = 9999;
    final double minSpeed = 0.01;
    final double maxSpeed = 0.99;
    final int maxStringLength = 50;


    public boolean isShipValid(Ship ship) {
        return ship != null && isStringValid(ship.getName()) && isStringValid(ship.getPlanet())
                && isProdDateValid(ship.getProdDate())
                && isSpeedValid(ship.getSpeed())
                && isCrewSizeValid(ship.getCrewSize());

    }

    public boolean isStringValid(String value) {
        return value != null && !value.isEmpty() && value.length() <= maxStringLength;
    }

    public boolean isCrewSizeValid(Integer crewSize) {
        return crewSize != null && crewSize.compareTo(minCrewSize) >= 0 && crewSize.compareTo(maxCrewSize) <= 0;
    }

    public boolean isSpeedValid(Double speed) {
        return speed != null && speed.compareTo(minSpeed) >= 0 && speed.compareTo(maxSpeed) <= 0;
    }

    public boolean isProdDateValid(Date prodDate) {
        final Date startProd = getDateForYear(start);
        final Date endProd = getDateForYear(now);
        return prodDate != null && prodDate.after(startProd) && prodDate.before(endProd);
    }

    private Date getDateForYear(int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

}
