package com.example.palianytsia.controller;

import com.example.palianytsia.controller.request.UserSignUpRequest;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.DuplicatedEmailException;
import com.example.palianytsia.exception.ServiceException;
import com.example.palianytsia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.palianytsia.controller.Constants.MSG;
import static com.example.palianytsia.controller.Constants.NO_MATCH;

@Slf4j
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/signIn")
    public String toSignIn() {
        return "signIn";
    }

    @GetMapping("/signUp")
    public String toSignUp(Model model) {
        model.addAttribute("userSignUpRequest", new UserSignUpRequest());
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute @Validated UserSignUpRequest userSignUpRequest, Model model) throws ServiceException {
        if(userSignUpRequest.getPassword().equals(userSignUpRequest.getRepeated_password())) {
            log.info("Passwords match");
            try {
                registerUser(userSignUpRequest);
            } catch (DuplicatedEmailException e) {
                log.info("Duplicated Email");
                model.addAttribute(MSG, e.getMessage());
                return "signUp";
            }
        } else {
            log.info("Passwords don't match");
            model.addAttribute(MSG, NO_MATCH);
            return "signUp";
        }
        return "redirect:/signIn";
    }
    @PostMapping("/signIn")
    public String signIn(@ModelAttribute @Validated UserSignUpRequest userSignUpRequest, Model model) {
        UserDTO userDTO=new UserDTO()
                .setEmail(userSignUpRequest.getEmail())
                .setPassword(userSignUpRequest.getPassword());
        try {
            userService.signIn(userDTO);
        } catch (ServiceException e) {
            model.addAttribute(MSG, e.getMessage());
        }
        return null;
    }

    private void registerUser(UserSignUpRequest userSignupRequest) throws ServiceException {
        UserDTO userDto = new UserDTO()
                .setEmail(userSignupRequest.getEmail())
                .setPassword(userSignupRequest.getPassword())
                .setFirstName(userSignupRequest.getFirstName())
                .setLastName(userSignupRequest.getLastName())
                .setMobileNumber(userSignupRequest.getMobileNumber());
        log.info("Successfully setting fields to UserDTO");
        userService.signUp(userDto);
    }

}
