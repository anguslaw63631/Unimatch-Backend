package com.unimatch.unimatch_backend.common.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import com.unimatch.unimatch_backend.common.shiro.util.SubjectUtil;
import com.unimatch.unimatch_backend.model.entity.User;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;


//MybatisPlus Auto Fill in the createBy, createTime, updateBy, updateTime fields
@Slf4j
@Component
public class ChatMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        User userInfo = SubjectUtil.getUserInfo();
        if (ObjectUtil.isNotNull(userInfo)) {
            this.setFieldValByName("createBy", userInfo.getId(), metaObject);
            this.setFieldValByName("updateBy", userInfo.getId(), metaObject);
        }
        this.setFieldValByName("createTime", DateUtil.date(), metaObject);
        this.setFieldValByName("updateTime", DateUtil.date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        User userInfo = SubjectUtil.getUserInfo();
        if (ObjectUtil.isNotNull(userInfo)) {
            this.setFieldValByName("updateBy", userInfo.getId(), metaObject);
        }
        this.setFieldValByName("updateTime", DateUtil.date(), metaObject);
    }
}
