package com.example.palianytsia.controller;

import com.example.palianytsia.controller.request.UserSignUpRequest;
import com.example.palianytsia.dto.RoleDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.DuplicatedEmailException;
import com.example.palianytsia.exception.ServiceException;
import com.example.palianytsia.model.Item;
import com.example.palianytsia.model.ItemType;
import com.example.palianytsia.model.UserRoles;
import com.example.palianytsia.service.ItemService;
import com.example.palianytsia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static com.example.palianytsia.controller.Constants.*;
import static com.example.palianytsia.controller.PageConstants.*;

@Slf4j
@Controller
@RequestMapping("/guest")
public class BasicGuestController {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

   private static final PaginationUtil paginationUtil=new PaginationUtil();

    @GetMapping("/mainPage")
    public String goHome(Model model, Locale locale) {
        model.addAttribute(LANG, locale.getLanguage());
        return MAIN_PAGE;
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute(COOKIES, itemService.displayThreeCookies());
        model.addAttribute(CAKES, itemService.displayThreeCakes());
        model.addAttribute(CROISSANTS, itemService.displayThreeCroissants());
        model.addAttribute(CUPCAKES, itemService.displayThreeCupcakes());
        model.addAttribute(CHEESECAKES, itemService.displayThreeCheesecakes());
        return PRODUCTS_PAGE;
    }

    @GetMapping("/allProducts")
    public String showAllProducts(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "6") int recordsPerPage,
                                  @RequestParam(defaultValue = ID) String sort,
                                  @RequestParam(defaultValue = ASC) String dir,
                                  @RequestParam(required = false, value = "type") String[] types) {

        System.out.println("Method allProducts");
        Sort sorting = Sort.by(sort);
        if (dir.equals(ASC)) {
            sorting = sorting.ascending();
        } else {
            sorting = sorting.descending();
        }

        List<ItemType> selectedTypes = new ArrayList<>();
        if (types != null) {
            for (String type : types) {
                for (String typePart : type.split(",")) {
                selectedTypes.add(ItemType.valueOf(typePart.toUpperCase())); }
            }
        }

        Pageable pageable = PageRequest.of(page, recordsPerPage, sorting);
        Page<Item> itemPage = itemService.displayAllItems(selectedTypes, pageable);
        Map<String, Object> response = paginationUtil.pagination(itemPage, dir);
        model.addAllAttributes(response);
        return ALL_PRODUCTS_PAGE;
    }


    @GetMapping("/allCookies")
    public String showCookies(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "6") int recordsPerPage,
                              @RequestParam(defaultValue = ID) String sort,
                              @RequestParam(defaultValue = ASC) String dir
                              ) {
        log.info("Page: "+page+" Records per page: "+recordsPerPage);
        Sort sorting = Sort.by(sort);
        if (dir.equals(ASC)) {
            sorting = sorting.ascending();
        } else {
            sorting = sorting.descending();
        }
        Pageable pageable = PageRequest.of(page, recordsPerPage, sorting);
        Page<Item> itemPage = itemService.displayCookies(pageable);
        Map<String, Object> response = paginationUtil.pagination(itemPage, dir);
        model.addAllAttributes(response);

        return COOKIES_PAGE;
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
