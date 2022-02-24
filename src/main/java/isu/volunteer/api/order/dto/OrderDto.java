package isu.volunteer.api.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import isu.volunteer.api.order.Address;
import isu.volunteer.api.order.Order;
import isu.volunteer.api.order.Status;
import isu.volunteer.api.user.User;
import isu.volunteer.api.user.dto.UserDto;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
    private Long id;

    private Address address;

    private String headline;
    private String comment;

    private LocalDateTime date;
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private UserDto performer;
    private UserDto owner;

    public OrderDto(Order order) {
        this.address = order.getAddress();
        this.id = order.getId();
        this.headline = order.getHeadline();
        this.comment =order.getComment();
        this.date = order.getDate();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.modifiedAt = order.getModifiedAt();
        this.performer = convertUserToUserDto(order.getPerformer());
        this.owner = convertUserToUserDto(order.getOwner());
    }

    protected UserDto convertUserToUserDto(User user) {
        if(user == null) {
            // TODO: dont return null;
            return null;
        }
        return new UserDto(user);
    }

    public static List<OrderDto> convertOrdersToOrderDtos(List<Order> orders) {
        return orders.stream().map(order -> new OrderDto(order)).collect(Collectors.toList());
    }
}
