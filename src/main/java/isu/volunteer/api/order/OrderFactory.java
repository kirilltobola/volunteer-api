package isu.volunteer.api.order;

import org.springframework.stereotype.Component;

@Component
public final class OrderFactory {
    public Order create() {
        return new Order();
    }
}
