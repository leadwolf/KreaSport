package com.ccaroni.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Master on 26/04/2017.
 */
@RestController
public class Demo {

    @RequestMapping("/")
    String hello() {
        return "hello world";
    }

}
