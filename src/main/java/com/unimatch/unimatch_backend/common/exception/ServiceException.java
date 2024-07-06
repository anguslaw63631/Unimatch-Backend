package com.unimatch.unimatch_backend.common.exception;

import lombok.Getter;
import lombok.Setter;
import com.unimatch.unimatch_backend.common.base.enums.AbstractBaseExceptionEnum;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 4L;

    @Getter
    @Setter
    private Integer errorCode;
    public ServiceException() {
        super();
    }
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(AbstractBaseExceptionEnum chatEnum) {
        super(chatEnum.getResultMsg());
        this.errorCode = chatEnum.getResultCode();
    }
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }
    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
