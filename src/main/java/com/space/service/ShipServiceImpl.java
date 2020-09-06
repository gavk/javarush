package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class ShipServiceImpl implements ShipService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public Iterable<Ship> getShips(ShipDTO shipDTO) {
        return preparePagination(shipDTO);
    }

    @Override
    public Optional<Ship> findById(Long id) {
        return shipRepository.findById(id);
    }

    @Override
    public Integer getCount(ShipDTO shipDTO) {
        CriteriaQuery<Ship> cq = prepareQuery(shipDTO);
        return em.createQuery(cq).getResultList().size();
    }

    public Ship save(Ship ship) {
        if (!ship.validate())
            return null;
        Ship result = shipRepository.save(ship);
        return result;
    }

    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

    public Ship update(Ship ship) {
        Ship result = shipRepository.saveAndFlush(ship);
        return result;
    }

    private CriteriaQuery<Ship> prepareQuery(ShipDTO shipDTO) {
        // Получаем сборщик критериев для поиска требуемых кораблей
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ship> cq = cb.createQuery(Ship.class);
        Predicate p = cb.conjunction();
        Root<Ship> root = cq.from(Ship.class);

        String name = shipDTO.getName();
        if (isValidString(name))
            p = cb.and(p, cb.like(root.get("name"), "%" + name + "%"));

        String planet = shipDTO.getPlanet();
        if (isValidString(planet))
            p = cb.and(p, cb.like(root.get("planet"), "%" + planet + "%"));

        ShipType shipType = shipDTO.getShipType();
        if (Objects.nonNull(shipType))
            p = cb.and(p, cb.equal(root.get("shipType"), shipType));

        Long after = shipDTO.getAfter();
        if (Objects.nonNull(after))
            p = cb.and(p, cb.greaterThanOrEqualTo(root.get("prodDate"), new java.sql.Date(after)));

        Long before = shipDTO.getBefore();
        if (Objects.nonNull(before))
            p = cb.and(p, cb.lessThanOrEqualTo(root.get("prodDate"), new java.sql.Date(before)));

        Boolean isUsed = shipDTO.getUsed();
        if (Objects.nonNull(isUsed))
            p = cb.and(p, cb.equal(root.get("isUsed"), isUsed));

        Double minSpeed = shipDTO.getMinSpeed();
        if (Objects.nonNull(minSpeed))
            p = cb.and(p, cb.greaterThanOrEqualTo(root.get("speed"), minSpeed));

        Double maxSpeed = shipDTO.getMaxSpeed();
        if (Objects.nonNull(maxSpeed))
            p = cb.and(p, cb.lessThanOrEqualTo(root.get("speed"), maxSpeed));

        Integer minCrewSize = shipDTO.getMinCrewSize();
        if (Objects.nonNull(minCrewSize))
            p = cb.and(p, cb.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize));

        Integer maxCrewSize = shipDTO.getMaxCrewSize();
        if (Objects.nonNull(maxCrewSize))
            p = cb.and(p, cb.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize));

        Double minRating = shipDTO.getMinRating();
        if (Objects.nonNull(minRating))
            p = cb.and(p, cb.greaterThanOrEqualTo(root.get("rating"), minRating));

        Double maxRating = shipDTO.getMaxRating();
        if (Objects.nonNull(maxRating))
            p = cb.and(p, cb.lessThanOrEqualTo(root.get("rating"), maxRating));

        cq.where(p);

        ShipOrder orderBy = shipDTO.getOrderBy();
        if (Objects.nonNull(orderBy))
            cq.orderBy(cb.asc(root.get(orderBy.getFieldName())));


        return cq;
    }

    private Iterable<Ship> preparePagination(ShipDTO shipDTO) {
        CriteriaQuery<Ship> cq = prepareQuery(shipDTO);

        Integer pageSize = shipDTO.getPageSize();
        if (Objects.isNull(pageSize))
            pageSize = 3;

        Integer pageNumber = shipDTO.getPageNumber();
        if (Objects.isNull(pageNumber))
            pageNumber = 0;

        Iterable<Ship> result = em.createQuery(cq)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize).getResultList();

        return result;
    }

    private boolean isValidString(String s) {
        return Objects.nonNull(s) && !s.isEmpty();
    }
}
