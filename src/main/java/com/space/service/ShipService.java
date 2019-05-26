package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipRequest;
import com.space.model.ShipType;

import java.util.List;

/**
 * Интерфейс сервиса для работы с ShipRepository
 */
public interface ShipService {
    List<Ship> getAllShips(int pageNumber, int pageSize, String order, String name, String planet, Long after, Long before, Integer minCrewSize, Integer maxCrewSize, Double minSpeed, Double maxSpeed, Double minRating, Double maxRating, ShipType shipType, Boolean isUsed);

    long count(String name, String planet, Long after, Long before, Integer minCrewSize, Integer maxCrewSize, Double minSpeed, Double maxSpeed, Double minRating, Double maxRating, ShipType shipType, Boolean isUsed);

    Ship getShipById(long id);
    Ship createShip(ShipRequest request);
    Ship updateShip(long id, ShipRequest request);
    void deleteShip(long id);
}
