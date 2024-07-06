package com.unimatch.unimatch_backend.model.entity;

import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;

@Data
public class GroupMsg extends BaseEntity {

    private Long groupId;

    private String msgContent;

    //Sys Msg:0,Text:1,Photo:2,Voice Msg:3,Video:4
    private byte msgType;

    private Long fromUserId;

    private String fromUserNickname;

    private String fromUserAvatar;

    //Voice Msg Length
    private String time;

    //Normal:0,Revoke:1,Delete:2
    private byte status;

}
