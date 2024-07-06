package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.GroupMsg;
import com.unimatch.unimatch_backend.model.vo.GroupMsgVo;


public interface GroupMsgService extends IService<GroupMsg> {

    //add a group message
    GroupMsgVo add(GroupMsgVo groupMsgVo);

    //group message list
    IPage<GroupMsgVo> page(GroupMsgVo groupMsgVo, Integer current, Integer size);

}
