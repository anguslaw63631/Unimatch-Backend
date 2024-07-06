package com.unimatch.unimatch_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;


@Data
@ApiModel("UserVo")
public class UserVo {

    @ApiModelProperty(value = "Primary Key")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "Username")
    private String username;

    @ApiModelProperty(value = "Auth Token")
    private String token;

    @ApiModelProperty(value = "Nickname")
    private String nickname;

    @ApiModelProperty(value = "Avatar")
    private String avatar;

    @ApiModelProperty(value = "Email")
    private String email;

    @ApiModelProperty(value = "Sex")
    private String sex;

    @ApiModelProperty(value = "Birthday")
    private String birthday;

    @ApiModelProperty(value = "Intro")
    private String intro;

    @ApiModelProperty(value = "Major")
    private short major;

    @ApiModelProperty(value = "Interest1")
    private short interest1;

    @ApiModelProperty(value = "Interest2")
    private short interest2;

    @ApiModelProperty(value = "Interest3")
    private short interest3;

    @ApiModelProperty(value = "Last Location")
    private short last_location;

    @ApiModelProperty(value = "Status(Normal:1,Deleted:0)")
    private Boolean deleted;

    @ApiModelProperty(value = "create time")
    private Date createTime;

    @ApiModelProperty(value = "update time")
    private Date updateTime;

}
