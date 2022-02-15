package isu.volunteer.api.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isu.volunteer.api.jwt.TokenUtil;
import isu.volunteer.api.user.User;
import isu.volunteer.api.user.UserService;


@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final TokenUtil tokenProvider;

    private final UserService userService;

    @Autowired
    public AuthController (AuthenticationManager authenticationManager, TokenUtil tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(@RequestBody AuthUserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        this.userService.add(user);

        String token = this.tokenProvider.createToken(userDto.getUsername(), user.getRoles());
        Map<String, Object> response = new HashMap<>();
        response.put("username", userDto.getUsername());
        response.put("token", token);
        response.put("expires in", this.tokenProvider.getExpirationByToken(token));

        return ResponseEntity.ok(response);
    }
    
    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody AuthUserDto userDto) {
        String username = userDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userDto.getPassword()));
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("not found");
        }
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);

        String token = this.tokenProvider.createToken(username, user.getRoles());
        response.put("token", token);
        response.put("expires in", this.tokenProvider.getExpirationByToken(token));

        return ResponseEntity.ok(response);
    }
}
