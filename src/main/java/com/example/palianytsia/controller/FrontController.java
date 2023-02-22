package com.example.palianytsia.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Slf4j
@Controller
public class FrontController {

    @GetMapping("/mainPage")
    public String goHome(Model model, Locale locale) {
        log.info("My main page");
        model.addAttribute("lang", locale.getLanguage());
        return "mainPage";
    }

//@GetMapping("/mainPage")
//public String goHome() {
//
//    return "mainPage";
//}
}
