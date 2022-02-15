package isu.volunteer.api.order;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import isu.volunteer.api.user.User;
import isu.volunteer.api.user.UserDto;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
    private Long id;

    // private Address address;

    private String headline;
    private String comment;

    private Date date;
    private Status status;

    private Date createdAt;
    private Date modifiedAt;

    private List<UserDto> users;
    private UserDto owner;

    public OrderDto(Order order) {
        // TODO: address;
        this.id = order.getId();
        this.headline = order.getHeadline();
        this.comment =order.getComment();
        this.date = order.getDate();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.modifiedAt = order.getModifiedAt();
        this.users = convertUsersToUserDtos(order.getUsers());
        this.owner = convertUserToUserDto(order.getOwner());
    }

    protected List<UserDto> convertUsersToUserDtos(List<User> users) {
        return users.stream().map(user -> new UserDto(user)).collect(Collectors.toList());
    }

    protected UserDto convertUserToUserDto(User user) {
        return new UserDto(user);
    }

    public static List<OrderDto> convertOrdersToOrderDtos(List<Order> orders) {
        return orders.stream().map(order -> new OrderDto(order)).collect(Collectors.toList());
    }
}
