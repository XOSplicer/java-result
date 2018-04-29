package result;

/**
 *
 * @author Felix Stegmaier
 */
public class UnwrapErrException extends RuntimeException {
    public final Object errValue;

    public UnwrapErrException(Object errValue) {
        this.errValue = errValue;
    }

    public UnwrapErrException(Object errValue, String message) {
        super(message);
        this.errValue = errValue;
    }

}
