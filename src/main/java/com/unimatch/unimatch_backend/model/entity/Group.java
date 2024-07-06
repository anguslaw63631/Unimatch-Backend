package com.unimatch.unimatch_backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;

import java.util.Date;

@Data
@TableName("`group`")
public class Group extends BaseEntity {

    private Date groupeventtime;
    private String groupeventlocation;

    private String name;

    private String avatar;

    private String notice;

    private String intro;

    private String Alphabetic;

    private Long adminUserId;

    private short tag;

    private short type;

    //Normal:1,Deleted:0
    private byte status;

}
