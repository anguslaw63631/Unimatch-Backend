package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.vo.BaseVo;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("FriendMsgVo")
public class FriendMsgVo extends BaseVo {

    @ApiModelProperty(value = "Message Content")
    @NotBlank(message = "Message Content cannot be empty")
    private String msgContent;

    @ApiModelProperty(value = "Message Type (Sys Msg:0,Text:1,Photo:2,Voice Msg:3,Video:4)")
    @NotBlank(message = "Message Type cannot be empty")
    private byte msgType;

    @ApiModelProperty(value = "Sender ID")
    @NotBlank(message = "Sender ID cannot be empty")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromUserId;

    @ApiModelProperty(value = "Receiver ID")
    @NotBlank(message = "Receiver ID cannot be empty")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toUserId;

    @ApiModelProperty(value = "Status (0-unread;1-read;2-withdraw;3-delete)")
    private byte status;

    @ApiModelProperty(value = "Source (0-Friend;1-Group)")
    private byte source;

    @ApiModelProperty(value = "Voice Message Duration")
    private String time;

}
