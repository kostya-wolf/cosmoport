package com.space.service;

import com.space.model.Ship;

import java.util.List;

/**
 * Интерфейс сервиса для работы с ShipRepository
 */
public interface ShipService {
    List<Ship> getAllShips();
    long count();
}
