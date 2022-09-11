package uz.md.trello.exceptions;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sat 27/08/22 18:34
 */
public class MessageNotSentException extends GenericRuntimeException {
    public MessageNotSentException(Exception e, Integer status) {
        super(e.getMessage(),status);
    }
}
