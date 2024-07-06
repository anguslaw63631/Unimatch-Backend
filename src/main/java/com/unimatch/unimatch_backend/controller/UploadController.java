package com.unimatch.unimatch_backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.common.exception.ServiceException;
import com.unimatch.unimatch_backend.common.util.FileUtils;
import com.unimatch.unimatch_backend.common.vo.FileVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@Api(tags = "File Upload Controller")
public class UploadController {

    @ApiOperation(value = "Upload File")
    @PostMapping("/upload")
    public ResponseData upload(MultipartFile file) {
        FileVo fileVo;
        try {
            fileVo = FileUtils.upload(file);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        return ResponseData.success(fileVo);
    }
}
