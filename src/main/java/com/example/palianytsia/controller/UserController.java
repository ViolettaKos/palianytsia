package com.example.palianytsia.controller;

import com.example.palianytsia.dto.LocationDTO;
import com.example.palianytsia.dto.OrderDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.DuplicatedEmailException;
import com.example.palianytsia.exception.ServiceException;
import com.example.palianytsia.model.City;
import com.example.palianytsia.model.Notifications;
import com.example.palianytsia.model.OrderStatus;
import com.example.palianytsia.service.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import static com.example.palianytsia.controller.Constants.*;
import static com.example.palianytsia.controller.EmailConstants.*;
import static com.example.palianytsia.controller.PageConstants.*;
import static org.springframework.util.StringUtils.hasLength;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    LocationService locationService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private EmailService emailService;
    private static final PaginationUtil paginationUtil = new PaginationUtil();


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
        checkIfSend(userDTO, ADD);
        redirectAttributes.addFlashAttribute(SUCCESS, WARN_ADDRESS);
        return REDIRECT_PROFILE;
    }

    private void checkIfSend(UserDTO userDTO, String type) {
            if(userDTO.getNotifications().isEmail_changes())
                sendEmail(userDTO, type);
    }

    private void sendEmail(UserDTO userDTO, String type) {
        String body="";
        switch (type) {
            case ADD ->
                body = String.format(MESSAGE_ADD, userDTO.getFirstName());

            case EDIT ->
                body = String.format(MESSAGE_EDIT, userDTO.getFirstName());

            case REMOVE ->
                body = String.format(MESSAGE_REMOVE, userDTO.getFirstName());

            case CHANGE ->
                body = String.format(MESSAGE_CHANGE, userDTO.getFirstName());

        }
        String finalBody = body;
        new Thread(() -> {
            try {
                emailService.sendEmail(userDTO.getEmail(), SBJ_CHANGES, finalBody);
            } catch (MessagingException e) {
                log.error("Error while sending email to user");
            }
        }).start();
    }

    @PostMapping("/editAddress")
    public String editAddress(@ModelAttribute LocationDTO locationDTO) {
        locationService.editAddress(locationDTO);
        return REDIRECT_PROFILE;
    }

    @PostMapping("/notifications")
    public String editNotifications(Principal principal, HttpServletRequest request) throws ServiceException {
        UserDTO userDTO=userService.findByEmail(principal.getName());
        Notifications notifications=userDTO.getNotifications();
        boolean emailChangesChecked = request.getParameter("notification").contains("email_changes");
        boolean emailInfoChecked = request.getParameter("notification").contains("email_info");
        boolean emailPromoChecked = request.getParameter("notification").contains("email_promo");
        notifications.setEmail_changes(emailChangesChecked).setEmail_info(emailInfoChecked).setEmail_promo(emailPromoChecked);
        userDTO.setNotifications(notifications);
        userService.updateProfile(userDTO.getEmail(), userDTO);

        return REDIRECT_PROFILE;
    }

    @PostMapping("/deleteAddress")
    public String deleteAddress(@ModelAttribute LocationDTO locationDTO, Principal principal) {
        locationService.deleteAddress(locationDTO);
        checkIfSend(userService.findByEmail(principal.getName()), REMOVE);
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
        checkIfSend(userService.findByEmail(principal.getName()), EDIT);
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
        checkIfSend(userService.findByEmail(principal.getName()), CHANGE);
        redirectAttributes.addFlashAttribute(SUCCESS, WARN_PASS);
        return REDIRECT_PROFILE;
    }

    @GetMapping("/checkoutPage")
    public String checkout(Model model, Principal principal) {
        UserDTO user = userService.findByEmail(principal.getName());
        model.addAttribute(USER, user);
        model.addAttribute(CITY, City.values());
        return CHECKOUT_PAGE;
    }

    @PostMapping("/makeOrder")
    public String makeOrder(Principal principal, HttpSession session,
                            @RequestParam(LOCATION) String location,
                            @RequestParam(value = CITY, required = false) String city,
                            @RequestParam(value = STREET, required = false) String street,
                            @RequestParam(value = HOUSE, required = false) String house,
                            @RequestParam(value = APARTMENT, required = false) String apartment) {
        String deliveryAddress;
        if (location.equals(OTHER)) {
            deliveryAddress = street + " " + house + " №" + apartment + ", " + city;
        } else {
            LocationDTO locationDTO = locationService.findById(Long.valueOf(location));
            deliveryAddress = locationDTO.toString();
        }
        UserDTO userDTO = userService.findByEmail(principal.getName());
        buildOrder(deliveryAddress, userDTO);
        shoppingCartService.clearCart();
        session.setAttribute(CART_COUNT, 0);
        return REDIRECT_ORDERS;
    }

    @GetMapping("/orderHistory")
    public String showOrderHistory(Principal principal, Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "6") int recordsPerPage,
                                   @RequestParam(defaultValue = ID) String sort,
                                   @RequestParam(defaultValue = DESC) String dir) {

        UserDTO user = userService.findByEmail(principal.getName());

        Sort sorting = Sort.by(sort);
        if (dir.equals(ASC)) {
            sorting = sorting.ascending();
        } else {
            sorting = sorting.descending();
        }
        Pageable pageable = PageRequest.of(page, recordsPerPage, sorting);
        Page<OrderDTO> orderDTOPage = userService.displayAllOrders(principal.getName(), pageable);

        model.addAttribute(USER, user);
        Map<String, Object> response = paginationUtil.paginationObj(orderDTOPage, dir);
        model.addAllAttributes(response);
        return ORDERS_PAGE;
    }


    private void buildOrder(String deliveryAddress, UserDTO userDTO) {
        OrderDTO orderDTO = new OrderDTO()
                .setOrderStatus(OrderStatus.IN_PROGRESS)
                .setTrackingNumber(UUID.randomUUID().toString())
                .setItems(shoppingCartService.getProductsInCart())
                .setTotalPrice(shoppingCartService.getTotal())
                .setDeliveryAddress(deliveryAddress)
                .setUserDTO(userDTO);
        orderService.putOrder(orderDTO);
    }
}
