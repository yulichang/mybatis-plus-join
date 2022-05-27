package com.github.yulichang.exception;

/**
 * mpj 异常
 *
 * @author yulichang
 */
public class MPJException extends RuntimeException {
    public MPJException(String message, Throwable cause) {
        super(message, cause);
    }

    public MPJException(String msg) {
        super(msg);
    }
}
