package com.example.palianytsia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @GetMapping("/mainPage")
    public String goHome() {
        return "mainPage";
    }
}
