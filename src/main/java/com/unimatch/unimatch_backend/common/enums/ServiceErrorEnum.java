package com.unimatch.unimatch_backend.common.enums;

import lombok.Getter;
import com.unimatch.unimatch_backend.common.base.enums.AbstractBaseExceptionEnum;

@Getter
public enum ServiceErrorEnum implements AbstractBaseExceptionEnum {

    CONFIRMATION_PASSWORD_ERROR(920, "Confirmation password error"),

    PASSWORD_IS_ERROR(910, "User name or password error"),
    USER_OLD_PASSWORD_ERROR(911, "Old password error"),
    USER_IS_EXIT(912, "User already exists"),
    USER_IS_NO_EXIT(913, "User does not exist"),

    FILE_IS_NULL(930, "File is null"),
    FILE_MAX_POST_SIZE(931, "File size exceeds limit"),
    FILE_UPLOAD_ERROR(932, "File upload error"),

    INVITATION_ALREADY_ERROR(940, "Invitation already exists"),
    FRIEND_ALREADY_ERROR(941, "Friend already exists"),

    MSG_REVOKE_TIMEOUT_ERROR(950, "Message has been sent for more than 3 minutes, cannot be revoked");

    private final Integer resultCode;
    private final String resultMsg;
    ServiceErrorEnum(Integer resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
