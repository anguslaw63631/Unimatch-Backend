package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.vo.BaseVo;



@Data
@ApiModel("InvitationVo")
public class InvitationVo extends BaseVo {

    @ApiModelProperty(value = "User Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "Friend Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long friendId;

    @ApiModelProperty(value = "User Nickname")
    private String userNickname;

    @ApiModelProperty(value = "Friend Nickname")
    private String friendNickname;

    @ApiModelProperty(value = "Avatar")
    private String avatar;

    @ApiModelProperty(value = "Friend Avatar")
    private String friendAvatar;

    @ApiModelProperty(value = "User Avatar")
    private String userAvatar;

    @ApiModelProperty(value = "Status(Waiting:0,Accepted:1,Rejected:2)")
    private byte status;

    @ApiModelProperty(value = "Rejection Reason")
    private String reason;

}
