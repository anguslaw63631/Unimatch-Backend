package com.unimatch.unimatch_backend.common.enums;

import lombok.Getter;
import com.unimatch.unimatch_backend.common.base.enums.AbstractBaseExceptionEnum;

@Getter
public enum HttpCodeEnum implements AbstractBaseExceptionEnum {

    SUCCESS(200, "Success"),
    BODY_NOT_MATCH(400, "Request body is not correct"),
    SIGNATURE_NOT_MATCH(401, "API signature does not match"),
    NOT_FOUND(404, "Resources not found"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    SERVER_BUSY(503, "Server is busy now, please try again later");

    private final Integer resultCode;
    private final String resultMsg;
    HttpCodeEnum(Integer resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}