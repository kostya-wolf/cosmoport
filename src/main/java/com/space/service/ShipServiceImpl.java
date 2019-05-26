package com.space.service;

import com.space.model.*;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.space.service.ShipSpecs.getSpecs;

/**
 * Реализация интерфейса сервиса для работы с ShipRepository
 */
@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    private ShipRepository shipRepository;

    @Override
    public List<Ship> getAllShips(int pageNumber, int pageSize, String order, String name, String planet, Long after, Long before, Integer minCrewSize, Integer maxCrewSize, Double minSpeed, Double maxSpeed, Double minRating, Double maxRating, ShipType shipType, Boolean isUsed) {
        return shipRepository.findAll(
                getSpecs(name, planet,
                        after, before,
                        minCrewSize, maxCrewSize,
                        minSpeed, maxSpeed,
                        minRating, maxRating,
                        shipType, isUsed),
                PageRequest.of(pageNumber, pageSize, Sort.by(order))).getContent();
    }

    @Override
    public long count(String name, String planet, Long after, Long before, Integer minCrewSize, Integer maxCrewSize, Double minSpeed, Double maxSpeed, Double minRating, Double maxRating, ShipType shipType, Boolean isUsed) {
        return shipRepository.count(
                getSpecs(name, planet,
                        after, before,
                        minCrewSize, maxCrewSize,
                        minSpeed, maxSpeed,
                        minRating, maxRating,
                        shipType, isUsed));
    }

    @Override
    public Ship getShipById(long id) {
        validateId(id);
        return shipRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Override
    public Ship createShip(ShipRequest request) {
        validateCreateRequest(request);
        Ship ship = new Ship();
        ship.setName(request.getName());
        ship.setPlanet(request.getPlanet());
        ship.setShipType(request.getShipType());
        ship.setProdDate(new Date(request.getProdDate()));
        ship.setUsed(request.getUsed() == null ? false : request.getUsed());
        ship.setSpeed(roundDouble(request.getSpeed()));
        ship.setCrewSize(request.getCrewSize());
        ship.setRating(calculateRating(request.getSpeed(), request.getUsed(), request.getProdDate()));
        return shipRepository.save(ship);
    }

    @Override
    public Ship updateShip(long id, ShipRequest request) {
        Ship ship = getShipById(id);
        String name = request.getName();
        if (name != null) {
            validateName(name);
            ship.setName(name);
        }
        String planet = request.getPlanet();
        if (planet != null) {
            validatePlanet(planet);
            ship.setPlanet(planet);
        }
        ShipType shipType = request.getShipType();
        if (shipType != null) {
            ship.setShipType(shipType);
        }
        Long prodDate = request.getProdDate();
        if (prodDate != null) {
            validateProdDate(prodDate);
            ship.setProdDate(new Date(prodDate));
        }
        if (request.getUsed() != null) {
            ship.setUsed(request.getUsed());
        }
        Double speed = request.getSpeed();
        if (speed != null) {
            validateSpeed(speed);
            ship.setSpeed(speed);
        }
        Integer crewSize = request.getCrewSize();
        if (crewSize != null) {
            validateCrewSize(crewSize);
            ship.setCrewSize(crewSize);
        }
        ship.setRating(calculateRating(request.getSpeed(), request.getUsed(), request.getProdDate()));
        return shipRepository.save(ship);
    }

    @Override
    public void deleteShip(long id) {
        validateId(id);
        shipRepository.deleteById(id);
    }

    /**
     * Метод рассчитывает рейтинг корабля
     * @param speed    скорость
     * @param used     использованный / новый
     * @param prodDate дата выпуска
     * @return рейтинг корабля
     */
    private Double calculateRating(double speed, boolean used, long prodDate) {
        Double roundedSpeed = roundDouble(speed);
        double k = used ? 0.5d : 1d;
        int thisYear = 3019;
        int prodYear = getYearFromDate(prodDate);
        double rating = (80 * roundedSpeed * k) / (thisYear - prodYear + 1);
        return roundDouble(rating);
    }

    /**
     * Метод округляет число Double до сотых
     * @param d число, которое нужно окргулить
     * @return округленное число
     */
    private double roundDouble(Double d) {
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Метод вычисляет год из переданной даты
     * @param prodDate дата в милисекундах
     * @return год
     */
    private int getYearFromDate(Long prodDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(prodDate);
        return calendar.get(Calendar.YEAR);
    }

    private void validateId(long id) {
        if (id < 1) {
            throw new BadRequestException("Не корректный id: " + id);
        }
    }

    private void validateCreateRequest(ShipRequest request) {
        if (request.getName() == null
                || request.getPlanet() == null
                || request.getShipType() == null
                || request.getProdDate() == null
                || request.getSpeed() == null
                || request.getCrewSize() == null) {
            throw new BadRequestException();
        }
        validateName(request.getName());
        validatePlanet(request.getPlanet());
        validateSpeed(request.getSpeed());
        validateCrewSize(request.getCrewSize());
        validateProdDate(request.getProdDate());
    }

    private void validateName(String name) {
        if ("".equals(name)) {
            throw new BadRequestException("Пустая строка name");
        }
        if (name.length() > 50) {
            throw new BadRequestException("Длина поля name превышает 50 символов");
        }
    }

    private void validatePlanet(String planet) {
        if ("".equals(planet)) {
            throw new BadRequestException("Пустая строка planet");
        }
        if (planet.length() > 50) {
            throw new BadRequestException("Длина поля planet превышает 50 символов");
        }
    }

    private void validateProdDate(Long prodDate) {
        if (prodDate < 0) {
            throw new BadRequestException("Дата выпуска < 0");
        }
        int prodYearMin = 2800;
        int prodYearMax = 3019;
        int prodYear = getYearFromDate(prodDate);
        if (prodYear < prodYearMin || prodYear > prodYearMax) {
            throw new BadRequestException("Год производства выходит за границы пределов");
        }
    }

    private void validateSpeed(Double speed) {
        double speedMin = 0.01d;
        double speedMax = 0.99d;
        double roundedSpeed = roundDouble(speed);
        if (roundedSpeed < speedMin || roundedSpeed > speedMax) {
            throw new BadRequestException("Скорость выходит за границы пределлов");
        }
    }

    private void validateCrewSize(Integer crewSize) {
        int crewSizeMin = 1;
        int crewSizeMax = 9999;
        if (crewSize < crewSizeMin || crewSize > crewSizeMax) {
            throw new BadRequestException("Размер команды выходит за границы пределлов");
        }
    }
}
