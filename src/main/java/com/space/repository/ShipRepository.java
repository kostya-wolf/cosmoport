package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Интерфейс репозитория для работы с сущностями Ship
 */
public interface ShipRepository extends CrudRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {
    long count(Specification<Ship> shipSpecification);

    Page<Ship> findAll(Specification<Ship> shipSpecification, Pageable pageable);
}
