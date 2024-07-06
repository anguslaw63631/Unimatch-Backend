package com.unimatch.unimatch_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.mapper.MsgUnreadRecordMapper;
import com.unimatch.unimatch_backend.model.entity.MsgUnreadRecord;
import com.unimatch.unimatch_backend.service.MsgUnreadRecordService;
import org.springframework.stereotype.Service;


@Service
public class MsgUnreadRecordServiceImpl extends ServiceImpl<MsgUnreadRecordMapper, MsgUnreadRecord> implements MsgUnreadRecordService {

    @Override
    public void update(MsgUnreadRecord msgUnreadRecord) {
        this.saveOrUpdate(msgUnreadRecord);
    }
}
