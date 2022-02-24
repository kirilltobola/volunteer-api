package isu.volunteer.api.message;

import org.springframework.stereotype.Component;

@Component
public final class MessageFactory {
    public Message create() {
        return new Message();
    }
}
