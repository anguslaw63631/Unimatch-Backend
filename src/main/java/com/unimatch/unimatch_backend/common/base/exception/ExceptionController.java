package com.unimatch.unimatch_backend.common.base.exception;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.common.enums.HttpCodeEnum;
import com.unimatch.unimatch_backend.common.exception.ServiceException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.UnexpectedTypeException;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseData<String> chatException(ServiceException ex) {
        log.error(Throwables.getStackTraceAsString(ex));
        return ResponseData.error(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public ResponseData<String> illegalStateException(IllegalStateException ex) {
        log.error(Throwables.getStackTraceAsString(ex));
        return ResponseData.error(HttpCodeEnum.SERVER_BUSY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseData<String> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        if (ex.hasErrors()) {
            ex.getAllErrors().forEach(error -> log.error(error.getDefaultMessage()));
        }
        return ResponseData.error(HttpCodeEnum.BODY_NOT_MATCH).setMessage(ex.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseData<String> illegalArgumentException(IllegalArgumentException ex) {
        log.error(Throwables.getStackTraceAsString(ex));
        return ResponseData.error(HttpCodeEnum.BODY_NOT_MATCH).setMessage(ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseData<String> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error(Throwables.getStackTraceAsString(ex));
        return ResponseData.error(HttpCodeEnum.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseBody
    public ResponseData<String> unexpectedTypeException(UnexpectedTypeException ex) {
        String errMsg = Throwables.getStackTraceAsString(ex);
        log.error(errMsg);
        return ResponseData.error(HttpCodeEnum.INTERNAL_SERVER_ERROR).setMessage(errMsg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData<String> exception(Exception ex) {
        String errMsg = Throwables.getStackTraceAsString(ex);
        log.error(errMsg);
        return ResponseData.error(HttpCodeEnum.INTERNAL_SERVER_ERROR).setMessage(ex.getCause().getMessage());
    }

}
