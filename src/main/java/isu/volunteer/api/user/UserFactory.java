package isu.volunteer.api.user;

import org.springframework.stereotype.Component;

@Component
public final class UserFactory {
    public User create() {
        return new User();
    }
}
