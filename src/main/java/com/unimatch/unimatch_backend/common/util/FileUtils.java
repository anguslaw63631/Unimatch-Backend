package com.unimatch.unimatch_backend.common.util;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import com.unimatch.unimatch_backend.common.enums.ServiceErrorEnum;
import com.unimatch.unimatch_backend.common.exception.ServiceException;
import com.unimatch.unimatch_backend.common.vo.FileVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@Slf4j
public class FileUtils {
    private static int MAX_POST_SIZE;

    @Value("${file.max-post-size}")
    public void setMaxPostSize(int maxPostSize) {
        MAX_POST_SIZE = maxPostSize;
    }

    private static String STATIC_PREFIX;

    @Value("${file.static-prefix}")
    public void setStaticPrefix(String staticPrefix) {
        STATIC_PREFIX = staticPrefix;
    }

    private static String UPLOAD_FOLDER;

    @Value("${file.upload-folder}")
    public void setUploadFolder(String uploadFolder) {
        UPLOAD_FOLDER = uploadFolder;
    }

    //Fixed data size
    static int FIXED_NUMBER = 1024;

    static String FIXED_SEPARATOR = "\\";

    public static FileVo upload(MultipartFile multipartFile) throws IOException {
        if (ObjectUtil.isEmpty(multipartFile) || multipartFile.getSize() <= 0) {
            throw new ServiceException(ServiceErrorEnum.FILE_IS_NULL);
        }
        long size = multipartFile.getSize();
        //File size limit
        if (size > MAX_POST_SIZE * FIXED_NUMBER * FIXED_NUMBER) {
            throw new ServiceException(ServiceErrorEnum.FILE_MAX_POST_SIZE);
        }
        // Set timestamp
        final String TIME_FOLDER = File.separator + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")) + File.separator + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")) + File.separator + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd")) + File.separator;
        final String fileFolder = UPLOAD_FOLDER + TIME_FOLDER;
        if (!FileUtil.exist(fileFolder)) {
            FileUtil.mkdir(fileFolder);
        }
        // Obtain UUID file name
        String fileName = getPathName(multipartFile);
        String path = fileFolder + fileName;
        File file = new File(path);
        if (FileUtil.exist(file)) {
            return getFileVo(multipartFile, TIME_FOLDER + fileName, file);
        }
        File file1 = FileUtil.writeBytes(multipartFile.getBytes(), path);
        if (file1.length() < 0) {
            throw new ServiceException(ServiceErrorEnum.FILE_UPLOAD_ERROR);
        }
        // Obtain URL
        String resultUrl = File.separator + STATIC_PREFIX + TIME_FOLDER + fileName;
        if (resultUrl.contains(FIXED_SEPARATOR)) {
            resultUrl = resultUrl.replaceAll("\\\\", "/");
        }
        return getFileVo(multipartFile, resultUrl, file);
    }

    private static String getPathName(MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private static String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
    }

    private static FileVo getFileVo(MultipartFile originalFile, String resUrl, File nowFile) {
        FileVo fileVo = FileVo.builder().nowFilename(FileNameUtil.getName(nowFile))
                .originalFilename(originalFile.getOriginalFilename())
                .extName(FileNameUtil.extName(nowFile))
                .fileType(FileTypeUtil.getType(nowFile))
                .fileSize(originalFile.getSize())
                .url(resUrl).build();
        return fileVo;
    }
}

