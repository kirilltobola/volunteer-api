package isu.volunteer.api.user;

public class UserNameInUseException extends RuntimeException {
    public UserNameInUseException(String message) {
        super(message);
    }
}
