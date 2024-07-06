package com.unimatch.unimatch_backend.common.base.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

@Accessors(chain = true)
@ApiModel("BaseEntityVo")
@Data
public class BaseVo implements Serializable {

    private static final long serialVersionUID = 3L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "primaryKey")
    private Long id;

    @ApiModelProperty("remark")
    private String remark;

    @ApiModelProperty(value = "deleted No:0 Yes:1")
    private Boolean deleted;

    @ApiModelProperty("createBy")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createBy;

    @ApiModelProperty(value = "createTime")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("updateBy")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateBy;

    @ApiModelProperty(value = "updateTime")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
