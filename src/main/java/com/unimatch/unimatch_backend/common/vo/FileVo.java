package com.unimatch.unimatch_backend.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("FileVo")
@Builder
public class FileVo implements Serializable {

    private static final long serialVersionUID = 5L;

    @ApiModelProperty("originalFilename")
    private String originalFilename;

    @ApiModelProperty("nowFilename")
    private String nowFilename;

    @ApiModelProperty("extName")
    private String extName;

    @ApiModelProperty("fileType")
    private String fileType;

    @ApiModelProperty("fileSize")
    private Long fileSize;

    @ApiModelProperty("fileUrl")
    private String url;

}
