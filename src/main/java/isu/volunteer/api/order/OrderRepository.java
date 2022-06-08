package isu.volunteer.api.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isu.volunteer.api.user.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findAllActiveOrders(@Param("status") Status status);

    @Query(value = "SELECT o FROM Order o WHERE o.owner = :user OR o.performer = :user")
    List<Order> findOrdersByUser(@Param("user") User user);

    @Query(value = "SELECT (o.performer != null) FROM Order o WHERE o = :order")
    Boolean hasPerformer(@Param("order") Order order);
}
