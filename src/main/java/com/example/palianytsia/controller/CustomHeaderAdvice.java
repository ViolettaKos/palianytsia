package com.example.palianytsia.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CustomHeaderAdvice {
    @ModelAttribute
    public void setHeaders(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json");
    }
}