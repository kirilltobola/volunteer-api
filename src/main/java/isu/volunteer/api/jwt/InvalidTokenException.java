package isu.volunteer.api.jwt;

public class InvalidTokenException extends Exception {

    public InvalidTokenException(String message) {
        super(message);
    }
}
