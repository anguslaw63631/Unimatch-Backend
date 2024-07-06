package com.unimatch.unimatch_backend.common.enums;

import lombok.Getter;
import com.unimatch.unimatch_backend.common.base.enums.AbstractBaseExceptionEnum;

@Getter
public enum CommonErrorEnum implements AbstractBaseExceptionEnum {
    SAVE_ERROR(900, "Save Error"),
    UPDATE_ERROR(901, "Update Error"),
    FIND_ERROR(902, "Find Error"),
    DELETE_ERROR(903, "Delete Error");

    private final Integer resultCode;
    private final String resultMsg;
    CommonErrorEnum(Integer resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

}