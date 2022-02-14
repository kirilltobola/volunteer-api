package isu.volunteer.api.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import isu.volunteer.api.user.User;
import isu.volunteer.api.user.UserService;

@Service
public class UserPrincipalService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if(user != null) {
            UserPrincipal userPrincipal = new UserPrincipal(
                user.getId(), 
                user.getUsername(), 
                user.getPassword(), 
                user.getFirstName(), 
                user.getLastName(), 
                user.getEmail(), 
                user.getEmail(), 
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
            );
            return userPrincipal;
        }
        throw new UsernameNotFoundException("not found");
    }
}
