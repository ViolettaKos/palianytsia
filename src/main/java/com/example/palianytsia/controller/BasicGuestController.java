package com.example.palianytsia.controller;

import com.example.palianytsia.controller.request.UserSignUpRequest;
import com.example.palianytsia.dto.RoleDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.DuplicatedEmailException;
import com.example.palianytsia.exception.ServiceException;
import com.example.palianytsia.model.Item;
import com.example.palianytsia.model.ItemType;
import com.example.palianytsia.model.Notifications;
import com.example.palianytsia.model.UserRoles;
import com.example.palianytsia.service.EmailService;
import com.example.palianytsia.service.ItemService;
import com.example.palianytsia.service.ShoppingCartService;
import com.example.palianytsia.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

import java.math.BigDecimal;
import java.util.*;

import static com.example.palianytsia.controller.Constants.*;
import static com.example.palianytsia.controller.EmailConstants.*;
import static com.example.palianytsia.controller.PageConstants.*;

@Slf4j
@Controller
@RequestMapping("/guest")
public class BasicGuestController {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private EmailService emailService;

    private static final PaginationUtil paginationUtil = new PaginationUtil();

    @GetMapping("/mainPage")
    public String goHome() {
        return MAIN_PAGE;
    }

    @GetMapping("/count")
    @ResponseBody
    public Integer count() {
        return shoppingCartService.getQty();
    }

    @GetMapping("/getCart")
    public String getCart(Model model) {
        model.addAttribute(ITEMS, shoppingCartService.getProductsInCart());
        BigDecimal total = shoppingCartService.getTotal();
        model.addAttribute(TOTAL, total);
        return CART_PAGE;
    }

    @PostMapping("/changeAmount")
    public String changeQty(@RequestParam(QTY) int qty, @RequestParam(ID) Long itemId, HttpServletRequest request) {
        Item item = itemService.findById(itemId);
        shoppingCartService.changeQty(item, qty);
        request.getSession().setAttribute(CART_COUNT, shoppingCartService.getQty());
        return REDIRECT_CART;
    }

    @PostMapping("/removeItem")
    public String removeItem(@RequestParam(ID) long id, HttpServletRequest request) {
        Item item = itemService.findById(id);
        shoppingCartService.removeItem(item);
        request.getSession().setAttribute(CART_COUNT, shoppingCartService.getQty());
        return REDIRECT_CART;
    }

    @PostMapping("/addItem")
    public String addItem(@RequestParam("itemId") Long itemId, @RequestParam(QTY) Integer qty,
                          RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Item item = itemService.findById(itemId);
        shoppingCartService.addItem(item, qty);
        redirectAttributes.addFlashAttribute(MSG, WARN_ADDED);

        String referer = request.getHeader("Referer");

        int cartCount = shoppingCartService.getQty();
        HttpSession session = request.getSession();
        session.setAttribute(CART_COUNT, cartCount);

        return "redirect:" + referer;
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
                    selectedTypes.add(ItemType.valueOf(typePart.toUpperCase()));
                }
            }
        }

        Pageable pageable = PageRequest.of(page, recordsPerPage, sorting);
        Page<Item> itemPage = itemService.displayAllItems(selectedTypes, pageable);
        Map<String, Object> response = paginationUtil.paginationObj(itemPage, dir);
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
        Sort sorting = Sort.by(sort);
        if (dir.equals(ASC)) {
            sorting = sorting.ascending();
        } else {
            sorting = sorting.descending();
        }
        Pageable pageable = PageRequest.of(page, recordsPerPage, sorting);
        Page<Item> itemPage = itemService.displayCookies(pageable);
        Map<String, Object> response = paginationUtil.paginationObj(itemPage, dir);
        model.addAllAttributes(response);

        return COOKIES_PAGE;
    }

    @GetMapping("/productPage")
    public String toProductPage(Model model, @RequestParam(ID) Long id) {
        log.info("Product id: " + id);
        model.addAttribute(ITEM, itemService.findById(id));
        return PRODUCT_PAGE;
    }

    @GetMapping("/signIn")
    public String toSignIn() {
        return SIGNIN_PAGE;
    }

    @GetMapping("/signUp")
    public String toSignUp(Model model) {
        model.addAttribute(USER_SIGNUP, new UserSignUpRequest());
        return SIGNUP_PAGE;
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute @Validated UserSignUpRequest userSignUpRequest, Model model,
                         RedirectAttributes redirectAttributes, HttpServletRequest req) throws ServiceException {
        if (userSignUpRequest.getPassword().equals(userSignUpRequest.getRepeated_password())) {
            log.info("Passwords match");
            try {
                registerUser(userSignUpRequest);
            } catch (DuplicatedEmailException e) {
                log.error("Duplicated Email");
                model.addAttribute(MSG, e.getMessage());
                return SIGNUP_PAGE;
            }
        } else {
            log.error("Passwords don't match");
            model.addAttribute(MSG, NO_MATCH);
            return SIGNUP_PAGE;
        }
        sendEmail(userSignUpRequest, req);
        redirectAttributes.addFlashAttribute(SUCCESS, WARN_SUCCESS);
        return REDIRECT_SIGNIN;
    }

    private void sendEmail(UserSignUpRequest userSignUpRequest, HttpServletRequest req) {
        String body = String.format(MESSAGE_REGISTER, userSignUpRequest.getFirstName(), getURL(req));
        new Thread(() -> {
            try {
                emailService.sendEmail(userSignUpRequest.getEmail(), SBJ_REG, body);
            } catch (MessagingException e) {
                log.error("Error while sending email to user");
            }
        }).start();
    }

    private String getURL(HttpServletRequest request) {
        return request.getRequestURL().toString()+request.getRequestURI();
    }

    private void registerUser(UserSignUpRequest userSignupRequest) throws ServiceException {
        RoleDTO userRole = new RoleDTO().setRole(UserRoles.USER);
        userRole.setId(2L);
        Set<RoleDTO> roles = new HashSet<>();
        roles.add(userRole);
        Notifications notifications=new Notifications().setEmail_changes(true).setEmail_info(true).setEmail_promo(false);
        UserDTO userDto = new UserDTO()
                .setRoles(roles)
                .setEmail(userSignupRequest.getEmail())
                .setPassword(userSignupRequest.getPassword())
                .setFirstName(userSignupRequest.getFirstName())
                .setLastName(userSignupRequest.getLastName())
                .setNotifications(notifications)
                .setMobileNumber(userSignupRequest.getMobileNumber());
        log.info("Successfully setting fields to UserDTO");
        userService.signUp(userDto);
    }

}
