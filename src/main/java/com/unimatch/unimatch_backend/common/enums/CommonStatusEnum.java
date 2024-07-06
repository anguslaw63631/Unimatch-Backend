package com.unimatch.unimatch_backend.common.enums;

import lombok.Getter;

@Getter
public enum CommonStatusEnum {

    NO((byte) 0, "No"),
    YES((byte) 1, "Yes"),

    INVITATION_WAIT((byte) 0, "Wainting"),
    INVITATION_PASS((byte) 1, "Accepted"),
    INVITATION_REFUSE((byte) 2, "Refused"),

    MSG_TYPE_SYSTEM((byte) 0, "system"),
    MSG_TYPE_TEXT((byte) 1, "text"),
    MSG_TYPE_IMAGE((byte) 2, "image"),
    MSG_TYPE_VOICE((byte) 3, "voice"),
    MSG_TYPE_VIDEO((byte) 4, "video"),

    MSG_STATUS_NORMAL((byte) 0, "Normal"),
    MSG_STATUS_REVOKE((byte) 1, "Revoked"),
    MSG_STATUS_DELETE((byte) 2, "Deleted"),

    GROUP_USER_CREATE((byte) 0, "Creator"),
    GROUP_USER_SCAN((byte) 1, "ByQRCode"),
    GROUP_USER_PULL((byte) 2, "ByInvite"),

    MSG_SOURCE_FRIEND((byte) 0, "Friend"),
    MSG_SOURCE_GROUP((byte) 1, "Group"),
    ;

    private final Byte resultCode;
    private final String resultMsg;
    CommonStatusEnum(Byte resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
