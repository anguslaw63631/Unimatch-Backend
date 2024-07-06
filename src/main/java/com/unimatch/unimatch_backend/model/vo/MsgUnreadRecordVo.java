package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;

@Data
@Accessors(chain = true)
@ApiModel("MsgUnreadRecordVo")
public class MsgUnreadRecordVo extends BaseEntity {

    @ApiModelProperty(value = "Target ID(friend/group)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long targetId;

    @ApiModelProperty(value = "User ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "Unread Number")
    private int unreadNum;

}
