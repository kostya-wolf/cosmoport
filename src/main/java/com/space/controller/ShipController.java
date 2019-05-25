package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipRequest;
import com.space.service.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер
 */
@RestController
@RequestMapping("/rest")
public class ShipController {
    @Autowired
    private ShipServiceImpl shipService;

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public List<Ship> getAllShips() {
        return shipService.getAllShips();
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public long count() {
        return shipService.count();
    }

    @RequestMapping(value = "/ships/{shipId}", method = RequestMethod.GET)
    public Ship getShipById(@PathVariable("shipId") long id) {
        return shipService.getShipById(id);
    }

    @RequestMapping(value = "/ships", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Ship createShip(@RequestBody ShipRequest request) {
        return shipService.createShip(request);
    }
}
