package com.example.palianytsia.controller;

import com.example.palianytsia.dto.LocationDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.DuplicatedEmailException;
import com.example.palianytsia.exception.ServiceException;
import com.example.palianytsia.model.City;
import com.example.palianytsia.service.LocationService;
import com.example.palianytsia.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;

import static com.example.palianytsia.controller.Constants.*;
import static com.example.palianytsia.controller.PageConstants.PROFILE_PAGE;
import static com.example.palianytsia.controller.PageConstants.REDIRECT_PROFILE;
import static org.springframework.util.StringUtils.hasLength;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    LocationService locationService;


    @GetMapping("/profile")
    public String toProfile(Model model, Principal principal, @ModelAttribute(MSG) String message) {
        log.info("Email of user: " + principal.getName());
        UserDTO userDTO = userService.findByEmail(principal.getName());
        model.addAttribute(USER, userDTO);
        model.addAttribute(CITY, City.values());
        model.addAttribute(MSG, message);

        return PROFILE_PAGE;
    }

    @PostMapping("/addAddress")
    public String addAddress(Principal principal, @ModelAttribute LocationDTO locationDTO, RedirectAttributes redirectAttributes) {
        UserDTO userDTO = userService.findByEmail(principal.getName());
        locationService.addAddress(userDTO.getEmail(), locationDTO);
        redirectAttributes.addFlashAttribute(SUCCESS, WARN_ADDRESS);
        return REDIRECT_PROFILE;
    }

    @PostMapping("/editAddress")
    public String editAddress(@ModelAttribute LocationDTO locationDTO) {
        locationService.editAddress(locationDTO);
        return REDIRECT_PROFILE;
    }

    @PostMapping("/deleteAddress")
    public String deleteAddress(@ModelAttribute LocationDTO locationDTO) {
        locationService.deleteAddress(locationDTO);
        return REDIRECT_PROFILE;
    }

    @PostMapping("/editProfile")
    public String editProfile(@Validated @ModelAttribute UserDTO userDTO, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes) throws ServiceException {
        if (bindingResult.hasErrors()) {
            log.error("binding errors");
            redirectAttributes.addFlashAttribute(MSG, EMPTY);
            return REDIRECT_PROFILE;
        }
        try {
            String email = userService.findByEmail(principal.getName()).getEmail();
            userService.updateProfile(email, userDTO);
        } catch (DuplicatedEmailException e) {
            log.error("Duplicated email");
            redirectAttributes.addFlashAttribute(MSG, e.getMessage());
        }
        redirectAttributes.addFlashAttribute(SUCCESS, WARN_PROFILE);
        return REDIRECT_PROFILE;
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute UserDTO userDTO, Principal principal, RedirectAttributes redirectAttributes) {
        if (!hasLength(userDTO.getNewPass()) || !hasLength(userDTO.getConfirmPass())) {
            log.error("empty data");
            redirectAttributes.addFlashAttribute(MSG, EMPTY);
            return REDIRECT_PROFILE;
        }
        if (!userDTO.getConfirmPass().equals(userDTO.getNewPass())) {
            log.error("Passwords don't match!");
            redirectAttributes.addFlashAttribute(MSG, NO_MATCH);
            return REDIRECT_PROFILE;
        }
        userDTO.setEmail(principal.getName());
        userService.updatePassword(userDTO);
        redirectAttributes.addFlashAttribute(SUCCESS, WARN_PASS);
        return REDIRECT_PROFILE;
    }
}
