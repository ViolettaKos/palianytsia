package com.example.palianytsia.controller;

import com.example.palianytsia.dto.LocationDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.model.City;
import com.example.palianytsia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import static com.example.palianytsia.controller.Constants.CITY;
import static com.example.palianytsia.controller.Constants.USER;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;



    @GetMapping("/profile")
    public String toProfile(Model model, Principal principal) {
        log.info("To profile page");
        log.info("Email of user: "+principal.getName());
        UserDTO userDTO = userService.findByEmail(principal.getName());
        model.addAttribute(USER, userDTO);
        model.addAttribute(CITY, City.values());
        return "user/profile";
    }

    @PostMapping("/addAddress")
    public String addAddress(Principal principal, @ModelAttribute LocationDTO locationDTO, Model model) {
        UserDTO userDTO = userService.findByEmail(principal.getName());

        locationDTO=userService.addAddress(userDTO, locationDTO);
        userDTO.getLocations().add(locationDTO);
        model.addAttribute(USER, userDTO);

        return "redirect:/user/profile";
    }

    @PostMapping("/editAddress")
    public String editAddress(@ModelAttribute LocationDTO locationDTO, Principal principal, Model model) {
        UserDTO userDTO = userService.findByEmail(principal.getName());
        userService.editAddress(locationDTO);
        model.addAttribute(USER, userDTO);

        return "redirect:/user/profile";
    }

}
