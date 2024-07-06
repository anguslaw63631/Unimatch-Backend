package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.MsgUnreadRecord;


public interface MsgUnreadRecordService extends IService<MsgUnreadRecord> {

    //update unread message record
    void update(MsgUnreadRecord msgUnreadRecord);
}
