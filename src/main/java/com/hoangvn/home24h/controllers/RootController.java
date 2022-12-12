package com.hoangvn.home24h.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RootController {

    @GetMapping(path = "/")
    public String hello() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        return "Hello visitor, today: " + format.format(new Date());
    }
}
