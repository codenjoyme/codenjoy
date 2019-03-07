package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

/**
 * @author Igor_Petrov@epam.com
 * Created at 3/6/2019
 */
@RestController
public class SampleController {

    @GetMapping("/now")
    public String now() {
        return ZonedDateTime.now().toString();
    }
}
