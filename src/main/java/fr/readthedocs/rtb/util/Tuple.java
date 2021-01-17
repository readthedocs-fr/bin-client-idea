package fr.readthedocs.rtb.util;

/**
 * Created by Hokkaydo on 11-01-2021.
 */
public class Tuple<T, U> {
    private final T t;
    private final U u;
    public Tuple(T t, U u) {
        this.t = t;
        this.u = u;
    }
    public T getT() {
        return t;
    }
    public U getU() {
        return u;
    }
}
