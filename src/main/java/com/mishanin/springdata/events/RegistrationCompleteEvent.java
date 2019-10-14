package com.mishanin.springdata.events;

import com.mishanin.springdata.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String token;
    private String pathUrl;

    public RegistrationCompleteEvent(User source, String pathUrl) {
        super(source);
        this.user=source;
        this.token=UUID.randomUUID().toString();
        this.pathUrl=pathUrl;
    }
}
