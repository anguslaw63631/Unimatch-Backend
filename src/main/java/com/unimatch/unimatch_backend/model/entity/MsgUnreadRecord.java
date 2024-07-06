package com.unimatch.unimatch_backend.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import com.unimatch.unimatch_backend.common.base.entity.BaseEntity;

@Data
@Accessors(chain = true)
@ApiModel("MsgUnreadRecordEntity")
public class MsgUnreadRecord extends BaseEntity {

    //Friend / Group ID
    private Long targetId;

    private Long userId;

    private int unreadNum;

    //0: friend, 1: group
    private byte source;

}
