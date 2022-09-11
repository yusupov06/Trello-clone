package uz.md.trello.config.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 04/09/22 11:39
 */

@Component
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public <T> void publishCustomEvent(final GenericEvent<T> genericEvent) {
        System.out.println("Publishing custom event. ");
        if (genericEvent.success) {
            applicationEventPublisher.publishEvent(genericEvent.getWhat());
        }
    }
}