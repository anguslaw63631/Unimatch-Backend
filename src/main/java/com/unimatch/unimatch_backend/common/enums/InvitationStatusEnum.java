package com.unimatch.unimatch_backend.common.enums;

import lombok.Getter;

@Getter
public enum InvitationStatusEnum {

    WAIT((byte) 0, "Waiting"),
    PASS((byte) 1, "Accepted"),
    REFUSE((byte) 2, "Refused");

    private final Byte resultCode;
    private final String resultMsg;
    InvitationStatusEnum(Byte resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
