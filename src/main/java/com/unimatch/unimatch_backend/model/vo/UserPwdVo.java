package com.unimatch.unimatch_backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@ApiModel("UserPwdVo")
public class UserPwdVo implements Serializable {

    @ApiModelProperty(value = "Old Password", required = true)
    @NotBlank(message = "Please enter your old password")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$", message = "Password must be 6-16 characters long and contain only letters, numbers, underscores, and hyphens")
    private String oldPwd;

    @ApiModelProperty(value = "New Password", required = true)
    @NotBlank(message = "Please enter your new password")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$", message = "Password must be 6-16 characters long and contain only letters, numbers, underscores, and hyphens")
    private String newPwd;

    @ApiModelProperty(value = "Confirm New Password", required = true)
    @NotBlank(message = "Please enter your new password again")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$", message = "Password must be 6-16 characters long and contain only letters, numbers, underscores, and hyphens")
    private String confirmPwd;

}
