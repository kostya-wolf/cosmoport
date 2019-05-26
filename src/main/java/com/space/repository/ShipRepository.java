package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Интерфейс репозитория для работы с сущностями Ship
 */
public interface ShipRepository extends CrudRepository<Ship, Long> {
    List<Ship> findAllBy(Pageable pageable);
}
