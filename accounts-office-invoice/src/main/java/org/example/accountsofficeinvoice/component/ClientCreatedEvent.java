package org.example.accountsofficeinvoice.component;

import org.springframework.context.ApplicationEvent;

public class ClientCreatedEvent extends ApplicationEvent {

    public ClientCreatedEvent(Object source) {
        super(source);
    }
}
