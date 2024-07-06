package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.vo.BaseVo;
import org.hibernate.validator.constraints.NotBlank;


@Data
@ApiModel("GroupMsgVo")
public class GroupMsgVo extends BaseVo {

    @ApiModelProperty(value = "Message Content")
    @NotBlank(message = "Message Content cannot be empty")
    private String msgContent;

    @ApiModelProperty(value = "Message Type (Sys Msg:0,Text:1,Photo:2,Voice Msg:3,Video:4)")
    @NotBlank(message = "Message Type cannot be empty")
    private byte msgType;

    @ApiModelProperty(value = "Message Sender")
    @NotBlank(message = "Message Sender cannot be empty")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromUserId;

    @ApiModelProperty(value = "Message Receiver")
    @NotBlank(message = "Message Receiver cannot be empty")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    @ApiModelProperty(value = "Sender Nickname")
    private String fromUserNickname;

    @ApiModelProperty(value = "Sender Avatar")
    private String fromUserAvatar;

    @ApiModelProperty(value = "Status (0-unread;1-read;2-withdraw;3-delete)")
    private byte status;

    @ApiModelProperty(value = "Message Source (0-Friend;1-Group)")
    private byte source;

    @ApiModelProperty(value = "Voice Message Duration")
    private String time;
}
