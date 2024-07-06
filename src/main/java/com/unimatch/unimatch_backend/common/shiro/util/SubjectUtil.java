package com.unimatch.unimatch_backend.common.shiro.util;

import lombok.extern.slf4j.Slf4j;
import com.unimatch.unimatch_backend.common.exception.ServiceException;
import com.unimatch.unimatch_backend.model.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;

@Slf4j
public class SubjectUtil {

    public static User getUserInfo() {
        Subject subject = null;
        try {
            subject = SecurityUtils.getSubject();
        } catch (UnavailableSecurityManagerException e) {
            log.info("user auth error,{}", e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
        return (User) subject.getPrincipal();
    }


    public static String getUsername() {
        return getUserInfo().getUsername();
    }
    public static String getAvatar() {
        return getUserInfo().getAvatar();
    }
    public static Long getUserId() {
        return getUserInfo().getId();
    }
}
