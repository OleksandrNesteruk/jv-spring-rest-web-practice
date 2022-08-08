package mate.academy.spring.controller;

import java.util.List;
import java.util.stream.Collectors;
import mate.academy.spring.mapper.DtoResponseMapper;
import mate.academy.spring.model.Order;
import mate.academy.spring.model.User;
import mate.academy.spring.model.dto.response.OrderResponseDto;
import mate.academy.spring.service.OrderService;
import mate.academy.spring.service.ShoppingCartService;
import mate.academy.spring.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final OrderService orderService;
    private final DtoResponseMapper<OrderResponseDto, Order>
            orderResponseMapper;

    public OrderController(ShoppingCartService shoppingCartService,
                           UserService userService,
                           OrderService orderService,
                           DtoResponseMapper<OrderResponseDto, Order>
                                   orderResponseMapper) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.orderService = orderService;
        this.orderResponseMapper = orderResponseMapper;
    }

    @GetMapping("/{id}")
    public List<OrderResponseDto> getOrderHistory(@PathVariable Long id) {
        User user = userService.get(id);
        return orderService.getOrdersHistory(user).stream()
                .map(orderResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/complete{userId}")
    public OrderResponseDto completeOrder(@PathVariable Long userId) {
        User user = userService.get(userId);
        return orderResponseMapper.toDto(orderService
                .completeOrder(shoppingCartService.getByUser(user)));
    }

}