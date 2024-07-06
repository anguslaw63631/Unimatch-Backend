package com.unimatch.unimatch_backend.common.base.response;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import com.unimatch.unimatch_backend.common.base.enums.AbstractBaseExceptionEnum;
import com.unimatch.unimatch_backend.common.enums.HttpCodeEnum;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "ResponseData")
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 2L;

    private boolean success = true;

    private Integer code;

    private String message;

    private T data;

    public ResponseData() {
    }

    public ResponseData(AbstractBaseExceptionEnum errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    public static ResponseData success() {
        return success(null);
    }

    public static ResponseData success(Object data) {
        ResponseData rb = new ResponseData();
        rb.setCode(HttpCodeEnum.SUCCESS.getResultCode());
        rb.setMessage(HttpCodeEnum.SUCCESS.getResultMsg());
        rb.setData(data);
        return rb;
    }

    public static ResponseData success(String msg) {
        ResponseData rb = new ResponseData();
        rb.setCode(HttpCodeEnum.SUCCESS.getResultCode());
        rb.setMessage(msg);
        rb.setData(null);
        return rb;
    }

    public static ResponseData error(AbstractBaseExceptionEnum errorInfo) {
        ResponseData rb = new ResponseData();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setData(null);
        rb.setSuccess(false);
        return rb;
    }

    public static ResponseData error(Integer code, String message) {
        ResponseData rb = new ResponseData();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setSuccess(false);
        return rb;
    }

    public static ResponseData error(String message) {
        ResponseData rb = new ResponseData();
        rb.setCode(-1);
        rb.setMessage(message);
        rb.setSuccess(false);
        return rb;
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}


