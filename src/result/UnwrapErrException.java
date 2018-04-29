/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
