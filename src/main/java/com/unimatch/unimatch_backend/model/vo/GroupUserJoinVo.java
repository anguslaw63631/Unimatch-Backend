package com.unimatch.unimatch_backend.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;


@Data
public class GroupUserJoinVo extends BaseEntity {

    @ApiModelProperty(value = "Group ID")
    private Long groupId;

    @ApiModelProperty(value = "User IDs, using comma to separate")
    private String userIds;
}
