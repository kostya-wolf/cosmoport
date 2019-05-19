package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
