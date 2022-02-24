package isu.volunteer.api.chat;

import org.springframework.stereotype.Component;

@Component
public final class ChatFactory {
    public Chat create() {
        return new Chat();
    }
}
