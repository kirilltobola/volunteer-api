package isu.volunteer.api.user;

public class PhoneInUseException extends RuntimeException {
    public PhoneInUseException(String msg) {
        super(msg);
    }
}
