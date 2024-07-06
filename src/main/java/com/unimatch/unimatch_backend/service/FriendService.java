package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.Friend;
import com.unimatch.unimatch_backend.model.vo.FriendVo;


public interface FriendService extends IService<Friend> {

    //add friend
    FriendVo add(FriendVo friendVo);

    //friend list
    IPage<FriendVo> page(FriendVo friendVo, Integer current, Integer size);

    //friend update
    boolean update(FriendVo friendVo);

    //friend detail
    FriendVo detail(String friendId);

    FriendVo isFriend(Long selfId,String friendId);

    //friend delete
    boolean delete(String id);

}
