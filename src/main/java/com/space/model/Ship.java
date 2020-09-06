package com.space.model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

@Entity(name = "ship")
public class Ship {

    private static final Calendar YEAR2800 = new GregorianCalendar(2800, 1, 1);
    private static final Calendar YEAR3019 = new GregorianCalendar(3019, 1, 1);

    private static final int MAX_LENGTH_STRING = 50;

    public Ship() {
    }

    public Ship(Long id, String name, String planet, ShipType shipType,
                Date prodDate, Boolean isUsed, Double speed, Integer crewSize,
                Double rating) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

    public Ship(String name, String planet, ShipType shipType,
                Date prodDate, Boolean isUsed, Double speed, Integer crewSize
    ) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String planet;

    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    private Date prodDate;

    private Boolean isUsed;

    private Double speed;

    private Integer crewSize;

    private Double rating;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlanet() {
        return planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public Double getRating() {
        return rating;
    }

    @Deprecated
    public boolean validate() {

        if (!isValidString(name) || name.length() > MAX_LENGTH_STRING || name.length() == 0)
            return false;

        if (!isValidString(planet) || planet.length() > MAX_LENGTH_STRING)
            return false;

        if (Objects.isNull(shipType))
            return false;

        if (Objects.isNull(prodDate) || prodDate.before(YEAR2800.getTime())
                || prodDate.after(YEAR3019.getTime()))
            return false;

        if (Objects.isNull(speed) || speed < 0.01 || speed > 0.99)
            return false;

        if (Objects.isNull(crewSize) || crewSize < 1 || crewSize > 9999)
            return false;

        if (Objects.isNull(isUsed))
            isUsed = false;

        calculateRating();

        return true;
    }

    public boolean isValidForCreate() {
        if (!isValidString(name, false))
            return false;

        if (!isValidString(planet, false))
            return false;

        if (shipType == null)
            return false;

        if (!isValidProdDate(prodDate, false))
            return false;

        if (!isValidSpeed(speed, false))
            return false;

        if (!isValidCrewSize(crewSize, false))
            return false;
        return true;
    }

    public boolean isValidForUpdate() {
        if (!isValidString(name, true))
            return false;

        if (!isValidString(planet, true))
            return false;

        if (!isValidProdDate(prodDate, true))
            return false;

        if (!isValidSpeed(speed, true))
            return false;

        if (!isValidCrewSize(crewSize, true))
            return false;

        /*if (name == null && planet == null && shipType == null && speed == null && crewSize == null && prodDate == null)
            return false;*/
        return true;
    }

    private boolean isValidSpeed(Double speed, boolean isNullable) {
        if (speed == null)
            return isNullable;
        return speed >= 0.01 && speed <= 0.99;
    }

    private boolean isValidProdDate(Date date, boolean isNullable) {
        if (date == null)
            return isNullable;
        return prodDate.after(YEAR2800.getTime()) && prodDate.before(YEAR3019.getTime());
    }

    private boolean isValidCrewSize(Integer crewSize, boolean isNullable) {
        if (crewSize == null)
            return isNullable;
        return crewSize >= 1 && crewSize <= 9999;
    }

    /**
     * Проверяет, является ли значение строки допустимым
     *
     * @param s          Проверяемая строка
     * @param isNullable true, если null является допустимым значением строки, false, если нет
     * @return true, если строка имеет допустимое значение, false, если нет
     */
    private boolean isValidString(String s, boolean isNullable) {
        if (s == null)
            return isNullable;
        return s.length() > 0 && s.length() <= MAX_LENGTH_STRING;
    }

    private boolean isValidString(String s) {
        return Objects.nonNull(s) && s.length() > 0 && s.length() <= 50;
    }

    private void calculateRating() {
        double k = isUsed ? 0.5 : 1;
        int prodDateYear = prodDate.getYear() + 1900;
        int year3019 = YEAR3019.get(Calendar.YEAR);
        rating = 80 * speed * k / (year3019 - prodDateYear + 1);
        BigDecimal bd = new BigDecimal(rating.toString());
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        rating = bd.doubleValue();
    }

    public void update(Ship newShip) {
        boolean changed = false;
        String name = newShip.getName();
        if (isValidString(name)) {
            this.name = name;
            changed = true;
        }

        String planet = newShip.getPlanet();
        if (isValidString(planet)) {
            this.planet = planet;
            changed = true;
        }

        ShipType shipType = newShip.getShipType();
        if (Objects.nonNull(shipType)) {
            this.shipType = shipType;
            changed = true;
        }

        Date prodDate = newShip.getProdDate();
        if (Objects.nonNull(prodDate)) {
            this.prodDate = prodDate;
            changed = true;
        }

        Boolean isUsed = newShip.getUsed();
        if (Objects.nonNull(isUsed)) {
            this.isUsed = isUsed;
            changed = true;
        }

        Double speed = newShip.getSpeed();
        if (Objects.nonNull(speed)) {
            this.speed = speed;
            changed = true;
        }

        Integer crewSize = newShip.getCrewSize();
        if (Objects.nonNull(crewSize)) {
            this.crewSize = crewSize;
            changed = true;
        }

        calculateRating();
    }
}
