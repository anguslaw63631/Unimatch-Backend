package com.unimatch.unimatch_backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("RegisterVo")
public class RegisterVo implements Serializable {

    @ApiModelProperty(value = "email prefix", required = true)
    @NotBlank(message = "Please enter your HKUST email prefix")
    private String username;
}
