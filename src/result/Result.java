package result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 *
 * @author Felix Stegmaier
 */
public class Result<T,E> {
    
    private enum Variant {
        OK,
        ERR
    }
    
    private final Variant variant;
    private final T okValue;
    private final E errValue;
    
    private Result(Variant v, T ok, E err) {
        this.variant = v;
        this.okValue = ok;
        this.errValue = err;
    }
    
    public static <T> Result<T, ?> Ok(T ok) {
        return new Result(Variant.OK, ok, null);
    }
    
    public static <E> Result<?, E> Err(E err) {
        return new Result(Variant.ERR, null, err);
    }
    
    public static <T, E extends Exception> Result<T, E> Try(ThrowingSupplier<T, E> f) {
        try {
            return (Result<T, E>) Result.Ok(f.get());
        } catch (Exception e) {
            return (Result<T, E>) Result.Err(e);
        }
    }
    
    public boolean isOk() {
        return Variant.OK == this.variant;
    }
    
    public boolean isErr() {
        return Variant.ERR == this.variant;
    }
    
    public Optional<T> ok() {
        if(!this.isOk()) {
            return Optional.empty();
        }
        return Optional.of(this.okValue);
    }
    
    public Optional<E> err() {
        if(!this.isErr()) {
            return Optional.empty();
        }
        return Optional.of(this.errValue);
    }
    
    public <U> Result<U, E> map(Function<? super T, U> f) {
        if(this.isErr()) {
            return (Result<U, E>) this;
        }
        return (Result<U, E>) Result.Ok(f.apply(this.okValue));
    }
    
    public <F> Result<T, F> mapErr(Function<? super E, F> f) {
        if(this.isOk()) {
            return (Result<T, F>) this;
        }
        return (Result<T, F>) Result.Err(f.apply(this.errValue));
    }

    public <U> Result<U, E> and(Result<U, E> other) {
        if(this.isErr()) {
            return (Result<U, E>) this;
        }
        return other;
    }
    
    public <U> Result<U, E> andThen(Function<? super T, Result<U, E>> f) {
        if(this.isErr()) {
            return (Result<U, E>) this;
        }
        return f.apply(this.okValue);
    }
    
    public <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> f) {
        return this.andThen(f);
    }
    
    public <F> Result<T, F> or(Result<T, F> other) {
        if(this.isOk()) {
            return (Result<T, F>) this;
        }
        return other;
    }
    
    public <F> Result<T, F> orElse(Function<? super E, Result<T, F>> f) {
        if(this.isOk()) {
            return (Result<T, F>) this;
        }
        return f.apply(this.errValue);
    }
    
    public T unwrap() throws UnwrapErrException {
        if(this.isErr()) {
            throw new UnwrapErrException(this.errValue);
        }
        return this.okValue;
    }
    
    public T unwrapOr(T option) {
        if(this.isErr()) {
            return option;
        }
        return this.okValue;
    }
    
    public T unwrapOrElse(Supplier<T> f) {
        if(this.isErr()) {
            return f.get();
        }
        return this.okValue;
    }
    
    public T expect(String msg) throws UnwrapErrException {
        if(this.isErr()) {
            throw new UnwrapErrException(this.errValue, msg);
        }
        return this.okValue;
    }
    
    public Stream<T> stream() {
        if(this.isErr()) {
            return Stream.empty();
        }
        return Stream.of(this.okValue);
    }
    
    public Stream<E> streamErr() {
        if(this.isOk()) {
            return Stream.empty();
        }
        return Stream.of(this.errValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.variant, this.okValue, this.errValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Result<?, ?> other = (Result<?, ?>) obj;
        if (this.variant != other.variant) {
            return false;
        }
        if (!Objects.equals(this.okValue, other.okValue)) {
            return false;
        }
        if (!Objects.equals(this.errValue, other.errValue)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(this.isOk()) {
            return "Result.Ok[" + this.okValue + "]";
        }
        return "Result.Err[" + this.errValue + "]";
    }
    
}

