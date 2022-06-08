package isu.volunteer.api.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isu.volunteer.api.jwt.TokenUtil;
import isu.volunteer.api.role.RoleDto;
import isu.volunteer.api.user.PhoneInUseException;
import isu.volunteer.api.user.User;
import isu.volunteer.api.user.UserFactory;
import isu.volunteer.api.user.UserNameInUseException;
import isu.volunteer.api.user.UserService;


@RestController
@RequestMapping(value = "/api/v1/auth")
@Validated
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtil tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFactory userFactory;


    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterUserDto userDto) {
        User user = this.userFactory.create();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        User userWithGivenUsername = userService.findByUsername(user.getUsername());
        if (userWithGivenUsername != null) {
            throw new UserNameInUseException("username in use");
        }
        User userWithGivenPhone = userService.findByPhone(user.getPhone());
        if (userWithGivenPhone != null) {
            throw new PhoneInUseException("phone in use");
        }

        this.userService.register(user, userDto.getIsMedic());

        Map<Object, Object> response = new HashMap<>();
        String token = this.tokenProvider.createToken(userDto.getUsername(), user.getRoles());
        Date expiresIn = this.tokenProvider.getExpirationByToken(token);

        return ResponseEntity.ok(
            new AuthDto(
                user.getId(), 
                user.getUsername(), 
                RoleDto.convertToRoleDto(user.getRoles()), new TokenDto(token, expiresIn)
            )
        );
    }
    
    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthUserDto userDto) {
        String username = userDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userDto.getPassword()));
        User user = userService.findByUsername(username);

        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);

        String token = this.tokenProvider.createToken(username, user.getRoles());
        Date expiresIn = this.tokenProvider.getExpirationByToken(token);

        return ResponseEntity.ok(
            new AuthDto(
                user.getId(), 
                user.getUsername(), 
                RoleDto.convertToRoleDto(user.getRoles()), new TokenDto(token, expiresIn)
            )
        );
    }
}
