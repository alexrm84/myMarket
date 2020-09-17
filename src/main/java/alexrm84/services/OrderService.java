package alexrm84.services;

import alexrm84.DTOs.OrderDto;
import alexrm84.entities.Order;
import alexrm84.entities.User;
import alexrm84.repositories.OrderRepository;
import alexrm84.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    private OrderRepository orderRepository;
    private Cart cart;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Order createOrder(User user, Map<String, String> params){
        Order order = new Order(user, params.get("phone"), params.get("address"));
        cart.getItems().values().stream().forEach(item -> order.addItem(item));
        cart.clear();
        order.setStatus(Order.Status.CREATED);
        return orderRepository.save(order);
    }

    public Order createOrder(User user, OrderDto orderDto){
        Order order = new Order(user, orderDto.getPhone(), orderDto.getAddress());
        cart.getItems().values().stream().forEach(item -> order.addItem(item));
        cart.clear();
        order.setStatus(Order.Status.CREATED);
        return orderRepository.save(order);
    }

    public List<Order> findAllByUser(Long userId){
        return orderRepository.findAllByUser_id(userId);
    }

    public List<Order> findAllByFiltering(Specification<Order> specification) {
        return orderRepository.findAll(specification);
    }

    public List<Order> findAllByFiltering(Specification<Order> specification, Sort sort) {
        return orderRepository.findAll(specification, sort);
    }

    public Order findOneById(Long id) {
        return orderRepository.getOne(id);
    }
}
