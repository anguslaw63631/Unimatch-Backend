package com.unimatch.unimatch_backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.vo.BaseVo;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("MsgHandleVo")
public class MsgHandleVo extends BaseVo {

    @ApiModelProperty(value = "Revoke:1,Delete:2")
    @NotBlank(message = "Type cannot be empty")
    private byte type;

    @ApiModelProperty(value = "Message Content")
    private MessageVo message;
}
