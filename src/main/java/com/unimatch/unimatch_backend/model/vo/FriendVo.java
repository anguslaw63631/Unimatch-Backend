package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.vo.BaseVo;

@Data
@ApiModel("FriendVo")
public class FriendVo extends BaseVo {

    @ApiModelProperty(value = "User ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "Friend ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long friendId;

    @ApiModelProperty(value = "Friend Nickname")
    private String nickname;

    @ApiModelProperty(value = "Friend Avatar")
    private String avatar;

    @ApiModelProperty(value = "Friend Alphabetic")
    private String alphabetic;

    @ApiModelProperty(value = "Status(Normal:1,Deleted:0)")
    private byte status;

}
