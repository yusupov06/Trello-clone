package uz.md.trello.exceptions;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 04/09/22 10:00
 */
public class InvalidPositionException extends GenericRuntimeException {

    public InvalidPositionException(String message, Integer statusCode) {
        super(message, statusCode);
    }
}
