package com.greenfoxacademy.springwebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/__test")
    public String test() {
        return "ok";
    }
}
