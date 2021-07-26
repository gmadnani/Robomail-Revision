package exceptions;

/**
 * An exception thrown when a mail that is already delivered attempts to be delivered again.
 */
@SuppressWarnings("serial")
public class MailAlreadyDeliveredException extends Throwable    {
    public MailAlreadyDeliveredException(){
        super("This mail has already been delivered!");
    }
}
