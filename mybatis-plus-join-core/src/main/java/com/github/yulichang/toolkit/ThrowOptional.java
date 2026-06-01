package com.github.yulichang.toolkit;

import com.github.yulichang.wrapper.interfaces.DoSomething;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public class ThrowOptional {

    private ThrowOptional() {
    }

    private DoSomething doSomething;

    public static ThrowOptional tryDo(DoSomething doSomething) {
        ThrowOptional optional = new ThrowOptional();
        Objects.requireNonNull(doSomething);
        optional.doSomething = doSomething;
        return optional;
    }

    public void catchDo() {
        try {
            this.doSomething.doIt();
        } catch (Throwable ignored) {
        }
    }

    public void catchDo(DoSomething doSomething) {
        try {
            this.doSomething.doIt();
        } catch (Throwable ignored) {
            try {
                doSomething.doIt();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void catchDo(Consumer<Throwable> consumer) {
        try {
            this.doSomething.doIt();
        } catch (Throwable throwable) {
            consumer.accept(throwable);
        }
    }

}
