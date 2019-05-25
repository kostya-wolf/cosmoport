package com.space.service;

import com.space.model.BadRequestException;
import com.space.model.NotFoundException;
import com.space.model.ShipRequest;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Реализация интерфейса сервиса для работы с ShipRepository
 */
@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    private ShipRepository shipRepository;

    @Override
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    @Override
    public long count() {
        return shipRepository.count();
    }

    @Override
    public Ship getShipById(long id) {
        if (id < 1) {
            throw new BadRequestException("Не корректный id: " + id);
        }
        return shipRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Override
    public Ship createShip(ShipRequest request) {
        validateRequest(request);
        Ship ship = new Ship();
        ship.setName(request.getName());
        ship.setPlanet(request.getPlanet());
        ship.setShipType(request.getShipType());
        ship.setProdDate(new Date(request.getProdDate()));
        ship.setUsed(request.getUsed());
        ship.setSpeed(roundDouble(request.getSpeed()));
        ship.setCrewSize(request.getCrewSize());
        ship.setRating(calculateRating(request));
        return shipRepository.save(ship);
    }

    /**
     * Метод рассчитывает рейтинг корабля
     * @param request входные данные корабля
     * @return рейтинг корабля
     */
    private Double calculateRating(ShipRequest request) {
        Double speed = roundDouble(request.getSpeed());
        double k = request.getUsed() ? 0.5d : 1d;
        int thisYear = 3019;
        int prodYear = getYearFromDate(request.getProdDate());
        double rating = (80 * speed * k) / (thisYear - prodYear + 1);
        return roundDouble(rating);
    }

    /**
     * Метод округляет число Double до сотых
     * @param d число, которое нужно окргулить
     * @return округленное число
     */
    private Double roundDouble(Double d) {
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

    private void validateRequest(ShipRequest request) {
        if (StringUtils.isEmpty(request.getName())
                || StringUtils.isEmpty(request.getPlanet())
                || request.getShipType() == null
                || request.getProdDate() == null
                || request.getSpeed() == null
                || request.getCrewSize() == null) {
            throw new BadRequestException();
        }
        if (request.getName().length() > 50) {
            throw new BadRequestException("Длина поля name превышает 50 символов");
        }
        if (request.getPlanet().length() > 50) {
            throw new BadRequestException("Длина поля planet превышает 50 символов");
        }
        double speedMin = 0.01d;
        double speedMax = 0.99d;
        double speed = roundDouble(request.getSpeed());
        if (speed < speedMin || speed > speedMax) {
            throw new BadRequestException("Скорость выходит за границы пределлов");
        }
        int crewSizeMin = 1;
        int crewSizeMax = 9999;
        int crewSize = request.getCrewSize();
        if (crewSize < crewSizeMin || crewSize > crewSizeMax) {
            throw new BadRequestException("Размер команды выходит за границы пределлов");
        }
        if (request.getProdDate() < 0) {
            throw new BadRequestException("Дата выпуска < 0");
        }
        int prodYearMin = 2800;
        int prodYearMax = 3019;
        int prodYear = getYearFromDate(request.getProdDate());
        if (prodYear < prodYearMin || prodYear > prodYearMax) {
            throw new BadRequestException("Год производства выходит за границы пределлов");
        }
    }
}
