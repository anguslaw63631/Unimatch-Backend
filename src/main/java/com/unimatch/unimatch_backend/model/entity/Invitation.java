package com.unimatch.unimatch_backend.model.entity;

import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;

@Data
public class Invitation extends BaseEntity {

    private Long userId;

    private Long friendId;

    private String userNickname;

    private String friendNickname;

    private String friendAvatar;

    private String userAvatar;

    //Waiting:0,Accepted:1,Rejected:2
    private byte status;

    //Reject Reason
    private String reason;
}
