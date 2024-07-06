package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;
import java.util.Date;
import java.util.List;


@Data
public class GroupVo extends BaseEntity {

    @ApiModelProperty(value = "Event Time")
    private Date groupeventtime;
    @ApiModelProperty(value = "Event Location")
    private String groupeventlocation;

    @ApiModelProperty(value = "Group Name")
    private String name;

    @ApiModelProperty(value = "Group Avatar")
    private String avatar;

    @ApiModelProperty(value = "Group Notice")
    private String notice;

    @ApiModelProperty(value = "Group Introduction")
    private String intro;

    @ApiModelProperty(value = "Group Name Alphabetic")
    private String Alphabetic;

    @ApiModelProperty(value = "Group Admin User ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long adminUserId;

    @ApiModelProperty(value = "Status(Normal:1,Deleted:0)")
    private byte status;

    @ApiModelProperty(value = "Group Tag")
    private short tag;

    @ApiModelProperty(value = "Group Type")
    private short type;

    @ApiModelProperty(value = "Group User List")
    private List<GroupUserVo> groupUsers;

}
