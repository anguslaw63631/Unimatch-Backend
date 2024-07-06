package com.unimatch.unimatch_backend.model.entity;

import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;

@Data
public class GroupUser extends BaseEntity {

    private Long groupId;

    private String groupName;

    private Long userId;

    private String userNickname;

    private String userAvatar;

    //No:0,Yes:1
    private byte adminable;

    //Creator:0,ByQRCode:1,ByInvite:2
    private byte source;

    //Normal:1,Deleted:0,Exited:-1
    private byte status;

}
