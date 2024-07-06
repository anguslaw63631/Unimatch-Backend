package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.Group;
import com.unimatch.unimatch_backend.model.vo.*;

public interface GroupService extends IService<Group> {

    //Add group
    GroupVo add(GroupVo groupVo);

    //Group List
    IPage<GroupVo> page(GroupUserVo groupUserVo, int current, int size);

    //Get All Group List
    IPage<GroupVo> getAllGroupList(int current, int size);

    //Get Group List By Tag
    IPage<GroupVo> getGroupListByTag(int current, int size,short groupTag);



    //Group Detail
    GroupVo detail(String groupId);

    //Invite user to group
    void pull(GroupUserJoinVo groupUserJoinVo);

    //Join group
    void join(GroupUserJoinVo groupUserJoinVo);

    //Revoke message from group
    void msgHandle(MsgHandleVo msgHandleVo);
}
