package com.unimatch.unimatch_backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@ApiModel("LoginVo")
public class LoginVo implements Serializable {

    @ApiModelProperty(value = "email prefix", required = true)
    @NotBlank(message = "Please enter your HKUST email prefix")
    private String username;

    @ApiModelProperty(value = "password", required = true)
    @NotBlank(message = "Please enter your password")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$", message = "Password must be 6-16 characters long and contain only letters, numbers, underscores, and hyphens")
    private String password;
}
