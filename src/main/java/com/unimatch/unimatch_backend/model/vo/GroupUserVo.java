package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;


@Data
public class GroupUserVo extends BaseEntity {

    @ApiModelProperty(value = "Group ID")
    private Long groupId;

    @ApiModelProperty(value = "Group Name")
    private String groupName;

    @ApiModelProperty(value = "User ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "User Nickname")
    private String userNickname;

    @ApiModelProperty(value = "User Avatar")
    private String userAvatar;

}
