package com.unimatch.unimatch_backend.common.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //file access path
    @Value("${file.access-path-pattern}")
    private String accessPathPattern;

    @Value("${file.upload-folder}")
    private String uploadFolder;

    //virtual path for file upload
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件上传
        uploadFolder = StringUtils.appendIfMissing(uploadFolder, File.separator);
        registry.addResourceHandler(accessPathPattern)
                .addResourceLocations("file:" + uploadFolder);
    }
}
