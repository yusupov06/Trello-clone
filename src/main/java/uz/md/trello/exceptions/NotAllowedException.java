package uz.md.trello.exceptions;

/**
*    Me: muhammadqodir
*    Project: Trello/IntelliJ IDEA
*    Date:Fri 26/08/22 17:52
*/

public class NotAllowedException extends GenericRuntimeException {

    public NotAllowedException(String message, Integer statusCode) {
        super(message, statusCode);
    }

}
