package alexrm84.controllers.rest;

import alexrm84.DTOs.OrderDto;
import alexrm84.DTOs.mappers.OrderMapper;
import alexrm84.entities.Order;
import alexrm84.entities.User;
import alexrm84.services.MailService;
import alexrm84.services.OrderService;
import alexrm84.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private OrderService orderService;
    private UserService userService;
    private MailService mailService;
    private OrderMapper orderMapper;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(Principal principal) {
        User user = userService.findByPhone(principal.getName());
        if (user != null) {
            return new ResponseEntity<>(orderService.findAllByUser(user.getId()).stream().map(o ->
                    orderMapper.toDto(o)).collect(Collectors.toList()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<OrderDto>(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<OrderDto> saveOrder(@RequestBody OrderDto orderDto) {
        User user = userService.save(orderDto);
        Order order = orderService.createOrder(user, orderDto);
        if (user.getEmail() != null) {
            mailService.sendOrderMail(order);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

