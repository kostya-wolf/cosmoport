package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipRequest;

import java.util.List;

/**
 * Интерфейс сервиса для работы с ShipRepository
 */
public interface ShipService {
    List<Ship> getAllShips(int pageNumber, int pageSize, String s, String order);
    long count();
    Ship getShipById(long id);
    Ship createShip(ShipRequest request);
    Ship updateShip(long id, ShipRequest request);
}
