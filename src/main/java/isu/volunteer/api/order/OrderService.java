package isu.volunteer.api.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Order save(Order order) {
        return this.orderRepository.save(order);
    }

    public Order findById(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    public void delete(Order order) {
        this.orderRepository.delete(order);
    }
}
