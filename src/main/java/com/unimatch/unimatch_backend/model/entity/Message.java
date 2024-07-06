package com.unimatch.unimatch_backend.model.entity;

import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;
import java.util.Date;

@Data
public class Message extends BaseEntity {

    //Friend:0, Group:1
    private byte source;

    //Sys Msg:0,Text:1,Photo:2,Voice Msg:3,Video:4
    private byte msgType;

    private Long targetId;

    private Long mineUserId;

    //Friend nickname or Group Name
    private String targetName;

    private String groupUserName;

    private String targetAvatar;

    private String lastContent;

    private String toUserId;

    private String fromUserId;

    private Date lastTime;

    //Sender in Group chat
    private String nickname;

    //Unread number
    private Integer unread;

   //0:unread, 1:read, 2:withdraw
    private byte status;

    //Voice msg duration
    private String time;
}
