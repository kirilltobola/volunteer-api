package isu.volunteer.api.user;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isu.volunteer.api.PropertiesCopier;
import isu.volunteer.api.jwt.TokenUtil;
import isu.volunteer.api.order.OrderService;
import isu.volunteer.api.order.dto.OrderDto;
import isu.volunteer.api.user.dto.ChangePasswordDto;
import isu.volunteer.api.user.dto.EditUserDto;
import isu.volunteer.api.user.dto.UserDto;

@Validated
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PropertiesCopier<User, EditUserDto> propertiesCopier;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getUserInfo(@PathVariable("id") User user) {
        Map<Object, Object> response = new HashMap<>();
        UserDto userDto = new UserDto(user);
        response.put("user", userDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = "/{id}/orders")
    public ResponseEntity<Object> getOrders(@PathVariable("id") User user) {
        Map<Object, Object> response = new HashMap<>();
        List<OrderDto> orders = OrderDto.convertOrdersToOrderDtos(
            this.orderService.findByUser(user)
        );
        response.put("username", user.getUsername());
        response.put("orders", orders);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> edit(
        @PathVariable("id") User user,
        @RequestBody @Valid EditUserDto userDto,
        @RequestHeader("Authorization") String token
    ) {
        Map<Object, Object> response = new HashMap<>();
        User issuer = this.tokenUtil.getUser(token);
        
        if(user.getId() == issuer.getId()) {
            this.propertiesCopier.copyProperties(user, userDto);

            user.setModifiedAt(LocalDateTime.now());
            this.userService.save(user);

            String newToken = this.tokenUtil.createToken(user.getUsername(), user.getRoles());
            response.put("token", newToken);
            response.put("expires in", this.tokenUtil.getExpirationByToken(newToken));
            response.put("status", "Successfully edited");
            return ResponseEntity.ok(response);
        }
        throw new AccessDeniedException("Access denied. You are not the user.");
    }

    // TODO: invalidate token
    @PatchMapping(path = "/{id}/change-password")
    public ResponseEntity<Map<Object, Object>> changePassword(
        @PathVariable("id") User user,
        @RequestBody @Valid ChangePasswordDto userDto,
        @RequestHeader("Authorization") String token
    ) {
        Map<Object, Object> response = new HashMap<>();
        User issuer = this.tokenUtil.getUser(token);

        if(user.getId() == issuer.getId()) {
            user.setPassword(userDto.getPassword());
            this.userService.changePassword(user);
            
            user.setModifiedAt(LocalDateTime.now());
            this.userService.save(user);

            String newToken = this.tokenUtil.createToken(user.getUsername(), user.getRoles());
            response.put("token", newToken);
            response.put("expires in", this.tokenUtil.getExpirationByToken(newToken));

            response.put("status", "Password successfully changed");
            return ResponseEntity.ok(response);
        }
        throw new AccessDeniedException("Access denied. You are not the user.");
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<Object, Object>> delete(
        @PathVariable("id") Long id,
        @RequestHeader("Authorization") String token
    ) {
        Map<Object, Object> response = new HashMap<>();
        User user = this.tokenUtil.getUser(token);
        if(user.getId() == id) {
            this.userService.deleteById(id);
            response.put("status", "User successfully deleted");
            return ResponseEntity.ok(response);
        }
        throw new AccessDeniedException("Access denied. You are not the user.");
    }
}
