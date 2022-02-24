package isu.volunteer.api.auth;

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
import isu.volunteer.api.user.User;
import isu.volunteer.api.user.UserFactory;
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
    public ResponseEntity<Object> register(@RequestBody @Valid AuthUserDto userDto) {
        User user = this.userFactory.create();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        this.userService.register(user);

        Map<Object, Object> response = new HashMap<>();
        String token = this.tokenProvider.createToken(userDto.getUsername(), user.getRoles());
        response.put("username", userDto.getUsername());
        response.put("token", token);
        response.put("expires in", this.tokenProvider.getExpirationByToken(token));

        return ResponseEntity.ok(response);
    }
    
    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthUserDto userDto) {
        String username = userDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userDto.getPassword()));
        User user = userService.findByUsername(username);

        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);

        String token = this.tokenProvider.createToken(username, user.getRoles());
        response.put("token", token);
        response.put("expires in", this.tokenProvider.getExpirationByToken(token));

        return ResponseEntity.ok(response);
    }
}
