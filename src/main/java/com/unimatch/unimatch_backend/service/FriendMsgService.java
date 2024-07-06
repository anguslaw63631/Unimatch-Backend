package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.FriendMsg;
import com.unimatch.unimatch_backend.model.vo.FriendMsgVo;
import com.unimatch.unimatch_backend.model.vo.MsgHandleVo;


public interface FriendMsgService extends IService<FriendMsg> {

    //add friend message
    FriendMsgVo add(FriendMsgVo friendMsgVo);

    //Friend message list
    IPage<FriendMsgVo> page(FriendMsgVo friendMsgVo, Integer current, Integer size);

    //Friend message revoke
    void msgHandle(MsgHandleVo msgHandleVo);
}
