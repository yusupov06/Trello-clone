package uz.md.trello.config.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 01/09/22 22:12
 */

@Getter
@Setter
public class GenericEvent<T> extends ApplicationEvent {

    private T what;
    protected boolean success;

    public GenericEvent(T what, boolean success){
        super(what);
        this.what = what;
        this.success = success;
    }

}
