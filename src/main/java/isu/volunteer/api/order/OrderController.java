package isu.volunteer.api.order;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isu.volunteer.api.PropertiesCopier;
import isu.volunteer.api.jwt.TokenUtil;
import isu.volunteer.api.order.dto.AddOrderDto;
import isu.volunteer.api.order.dto.EditStatusDto;
import isu.volunteer.api.order.dto.OrderDto;
import isu.volunteer.api.user.User;

@RestController
@RequestMapping(value = "/api/v1/orders")
@Validated
public class OrderController {
    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFactory orderFactory;

    @Autowired
    private PropertiesCopier<Order, AddOrderDto> propertiesCopier;


    @GetMapping()
    public ResponseEntity<Object> getActiveOrders() {
        Map<Object, Object> response = new HashMap<>();
        response.put(
            "orders",
            OrderDto.convertOrdersToOrderDtos(this.orderService.findActive())
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getOrder(@PathVariable("id") Order order) {
        OrderDto orderDto = new OrderDto(order);
        
        Map<Object, Object> response = new HashMap<>();
        response.put("order", orderDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Object> addOrder(
        @RequestBody @Valid AddOrderDto orderDto,
        @RequestHeader("Authorization") String token
    ) {
        Order order = this.orderFactory.create();
        this.propertiesCopier.copyProperties(order, orderDto);

        User user = this.tokenUtil.getUser(token);
        order.setOwner(user);

        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        order.setModifiedAt(now);
        this.orderService.save(order);

        Map<Object, Object> response = new HashMap<>();
        response.put("status", "Successfully added");
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> editOrder(
        @PathVariable("id") Order order,
        @RequestBody @Valid AddOrderDto orderDto,
        @RequestHeader("Authorization") String token
    ) {
        Map<Object, Object> response = new HashMap<>();

        User user = this.tokenUtil.getUser(token);
        if(order.getOwner().equals(user)) {
            this.propertiesCopier.copyProperties(order, orderDto);
            
            order.setModifiedAt(LocalDateTime.now());
            this.orderService.save(order);
            response.put("status", "successfully edited");
            return ResponseEntity.ok(response);
        }
        throw new AccessDeniedException("You are not owner of the order.");
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> editStatus(
        @PathVariable("id") Order order,
        @RequestBody @Valid EditStatusDto orderDto,
        @RequestHeader("Authorization") String token
    ) {
        Map<Object, Object> response = new HashMap<>();
        User user = this.tokenUtil.getUser(token);

        if(order.getOwner().equals(user)) {
            order.setStatus(Status.valueOf(orderDto.getStatus()));
            order.setModifiedAt(LocalDateTime.now());
            this.orderService.save(order);
            response.put("status", "status successfully edited");
            return ResponseEntity.ok(response);
        }
        throw new AccessDeniedException("You are not owner of the order.");
    }

    @PatchMapping(path = "/{id}/accept")
    public ResponseEntity<Object> respondTo(
        @PathVariable("id") Order order,
        @RequestHeader("Authorization") String token
    ) {
        Map<Object, Object> response = new HashMap<>();
        User user = this.tokenUtil.getUser(token);

        if(order.getPerformer() == null && !order.getOwner().equals(user)) {
            order.setPerformer(user);
            order.setModifiedAt(LocalDateTime.now());
            this.orderService.save(order);

            response.put("status", "performer successfully added");
            return ResponseEntity.ok(response);
        }
        throw new AccessDeniedException("You cannot respond to the order.");
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> delete(
        @PathVariable("id") Order order,
        @RequestHeader("Authorization") String token
    ) {
        Map<Object, Object> response = new HashMap<>();
        User user = this.tokenUtil.getUser(token);

        if(order.getOwner().equals(user)) {
            this.orderService.delete(order);
            response.put("status", "order successfully deleted");
            return ResponseEntity.ok(response);
        }
        throw new AccessDeniedException("You are not owner of the order.");
    }
}
