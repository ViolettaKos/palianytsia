package com.example.palianytsia.controller;

import com.example.palianytsia.controller.request.UserSignUpRequest;
import com.example.palianytsia.dto.RoleDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.DuplicatedEmailException;
import com.example.palianytsia.exception.ServiceException;
import com.example.palianytsia.model.UserRoles;
import com.example.palianytsia.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

import static com.example.palianytsia.controller.Constants.*;
import static com.example.palianytsia.controller.PageConstants.*;

@Slf4j
@Controller
@RequestMapping("/guest")
public class BasicGuestController {

    @Autowired
    UserService userService;

    @GetMapping("/mainPage")
    public String goHome(Model model, Locale locale) {
        model.addAttribute(LANG, locale.getLanguage());
        return MAIN_PAGE;
    }

    @GetMapping("/signIn")
    public String toSignIn() {
        log.info("To sign in page");
        return SIGNIN_PAGE;
    }



    @GetMapping("/signUp")
    public String toSignUp(Model model) {
        model.addAttribute(USER_SIGNUP, new UserSignUpRequest());
        return SIGNUP_PAGE;
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute @Validated UserSignUpRequest userSignUpRequest, Model model, RedirectAttributes redirectAttributes) throws ServiceException {
        if(userSignUpRequest.getPassword().equals(userSignUpRequest.getRepeated_password())) {
            log.info("Passwords match");
            try {
                registerUser(userSignUpRequest);
            } catch (DuplicatedEmailException e) {
                log.info("Duplicated Email");
                model.addAttribute(MSG, e.getMessage());
                return SIGNUP_PAGE;
            }
        } else {
            log.info("Passwords don't match");
            model.addAttribute(MSG, NO_MATCH);
            return SIGNUP_PAGE;
        }
        redirectAttributes.addFlashAttribute(SUCCESS, WARN_SUCCESS);
        return REDIRECT_SIGNIN;
    }

    private void registerUser(UserSignUpRequest userSignupRequest) throws ServiceException {
        RoleDTO userRole = new RoleDTO().setRole(UserRoles.USER);
        userRole.setId(2L);
        Set<RoleDTO> roles = new HashSet<>();
        roles.add(userRole);
        UserDTO userDto = new UserDTO()
                .setRoles(roles)
                .setEmail(userSignupRequest.getEmail())
                .setPassword(userSignupRequest.getPassword())
                .setFirstName(userSignupRequest.getFirstName())
                .setLastName(userSignupRequest.getLastName())
                .setMobileNumber(userSignupRequest.getMobileNumber());
        log.info("Successfully setting fields to UserDTO");
        userService.signUp(userDto);
    }

}
