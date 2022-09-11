package uz.md.trello.exceptions;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 15:44
 */
public class NotDeletedException extends GenericRuntimeException{
    public NotDeletedException(String message, Integer statusCode) {
        super(message, statusCode);
    }
}
