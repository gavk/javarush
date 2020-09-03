package com.space.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ship")
public class Ship {

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

}
