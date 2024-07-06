package com.unimatch.unimatch_backend.model.entity;

import lombok.Data;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;

@Data
public class Friend extends BaseEntity {

    private Long userId;

    private Long friendId;

    private String nickname;

    private String avatar;

    private String alphabetic;

    //Normal:1,Deleted:0
    private byte status;

}
