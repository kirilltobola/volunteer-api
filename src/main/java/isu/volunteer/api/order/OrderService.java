package isu.volunteer.api.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isu.volunteer.api.user.User;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public List<Order> findActive() {
        return this.orderRepository.findAllActiveOrders(Status.ACTIVE);
    }

    public List<Order> findByUser(User user) {
        return this.orderRepository.findOrdersByUser(user);
    }

    public Order save(Order order) {
        order.setModifiedAt(LocalDateTime.now());
        return this.orderRepository.save(order);
    }

    public Order findById(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    public void delete(Order order) {
        this.orderRepository.delete(order);
    }
}
