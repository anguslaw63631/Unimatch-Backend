package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("MessageVo")
public class MessageVo implements Serializable {

    //Friend:0, Group:1
    private byte source;

    //Sys Msg:0,Text:1,Photo:2,Voice Msg:3,Video:4
    private byte msgType;


    @JsonSerialize(using = ToStringSerializer.class)
    private Long targetId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long mineUserId;

    //Friend/Group
    private String targetName;

    private String targetAvatar;

    private String lastContent;

    private String toUserId;

    private String fromUserId;

    //Last Msg Time
    private Date lastTime;

    //Used in Group chat
    private String nickname;

    //unread number
    private Long unread;

    //0:unread,1:read,2:withdraw
    private byte status;
}
