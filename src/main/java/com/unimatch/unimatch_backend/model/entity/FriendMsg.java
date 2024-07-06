package com.unimatch.unimatch_backend.model.entity;

import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;

@Data
public class FriendMsg extends BaseEntity {

    private String msgContent;

    //Sys Msg:0,Text:1,Photo:2,Voice Msg:3,Video:4
    private byte msgType;

    private Long fromUserId;

    private Long toUserId;

    //Voice Msg Length
    private String time;

    //Normal:0,Revoke:1
    private byte status;
}
