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
    public List<Ship> getAllShips(@RequestParam(defaultValue = "0") int pageNumber,
                                  @RequestParam(defaultValue = "3") int pageSize,
                                  @RequestParam(defaultValue = "id") String order,
                                  @RequestParam(required = false) String name
                                  ) {
        return shipService.getAllShips(pageNumber, pageSize, order, name);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public long count() {
        return shipService.count();
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET)
    public Ship getShipById(@PathVariable("id") long id) {
        return shipService.getShipById(id);
    }

    @RequestMapping(value = "/ships", method = RequestMethod.POST)
    public Ship createShip(@RequestBody ShipRequest request) {
        return shipService.createShip(request);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST)
    public Ship updateShip(@PathVariable("id") long id, @RequestBody ShipRequest request) {
        return shipService.updateShip(id, request);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE)
    public void deleteShip(@PathVariable("id") long id) {
        shipService.deleteShip(id);
    }
}
