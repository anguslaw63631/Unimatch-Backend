package com.unimatch.unimatch_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.common.enums.CommonStatusEnum;
import com.unimatch.unimatch_backend.common.shiro.util.SubjectUtil;
import com.unimatch.unimatch_backend.mapper.GroupMsgMapper;
import com.unimatch.unimatch_backend.model.entity.GroupMsg;
import com.unimatch.unimatch_backend.model.entity.GroupUser;
import com.unimatch.unimatch_backend.model.entity.MsgUnreadRecord;
import com.unimatch.unimatch_backend.model.vo.GroupMsgVo;
import com.unimatch.unimatch_backend.service.GroupMsgService;
import com.unimatch.unimatch_backend.service.GroupUserService;
import com.unimatch.unimatch_backend.service.MsgUnreadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class GroupMsgServiceImpl extends ServiceImpl<GroupMsgMapper, GroupMsg> implements GroupMsgService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Resource
    private MsgUnreadRecordService msgUnreadRecordService;

    @Resource
    private GroupUserService groupUserService;

    @Override
    public GroupMsgVo add(GroupMsgVo groupMsgVo) {
        // Set group member unread num
        LambdaQueryWrapper<GroupUser> groupUserWrapper = new LambdaQueryWrapper<>();
        groupUserWrapper.eq(GroupUser::getGroupId, groupMsgVo.getGroupId());
        groupUserService.list(groupUserWrapper).forEach(groupUser -> {
            if (NumberUtil.equals(groupMsgVo.getFromUserId(), groupUser.getUserId())) {
                return;
            }
            // Query unread num
            LambdaQueryWrapper<MsgUnreadRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MsgUnreadRecord::getUserId, groupUser.getUserId()).eq(MsgUnreadRecord::getTargetId, groupMsgVo.getGroupId());
            MsgUnreadRecord msgUnreadRecord = msgUnreadRecordService.getOne(queryWrapper);
            if (ObjectUtil.isNull(msgUnreadRecord)) {
                msgUnreadRecord = new MsgUnreadRecord();
                msgUnreadRecord.setUnreadNum(1);
                msgUnreadRecord.setUserId(groupUser.getUserId());
                msgUnreadRecord.setTargetId(groupMsgVo.getGroupId());
                msgUnreadRecord.setSource(CommonStatusEnum.MSG_SOURCE_GROUP.getResultCode());
            } else {
                msgUnreadRecord.setUnreadNum(msgUnreadRecord.getUnreadNum() + 1);
            }
            msgUnreadRecordService.update(msgUnreadRecord);
        });
        // vo->entity
        GroupMsg groupMsg = new GroupMsg();
        BeanUtil.copyProperties(groupMsgVo, groupMsg);
        if (baseMapper.insert(groupMsg) > 0) {
            groupMsgVo.setId(groupMsg.getId());
            groupMsgVo.setSource(CommonStatusEnum.MSG_SOURCE_GROUP.getResultCode());
            messagingTemplate.convertAndSend("/topic/message/" + groupMsg.getGroupId(), JSONUtil.toJsonStr(groupMsgVo));
        }
        // entity->vo
        GroupMsgVo resGroupMsgVo = new GroupMsgVo();
        BeanUtil.copyProperties(groupMsg, resGroupMsgVo);
        return resGroupMsgVo;
    }

    @Override
    public IPage<GroupMsgVo> page(GroupMsgVo groupMsgVo, Integer current, Integer size) {
        IPage<GroupMsg> page = new Page<>(current, size);
        LambdaQueryWrapper<GroupMsg> query = new LambdaQueryWrapper<>();
        query.eq(GroupMsg::getGroupId, groupMsgVo.getGroupId());
        query.orderByDesc(GroupMsg::getCreateTime);
        IPage<GroupMsg> groupMsgs = this.page(page, query);
        // IPage<entity>->IPage<vo>
        IPage<GroupMsgVo> convert = groupMsgs.convert(GroupMsg -> BeanUtil.copyProperties(GroupMsg, GroupMsgVo.class));
        // Query unread num
        LambdaQueryWrapper<MsgUnreadRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsgUnreadRecord::getUserId, SubjectUtil.getUserId()).eq(MsgUnreadRecord::getTargetId, groupMsgVo.getGroupId());
        MsgUnreadRecord msgUnreadRecord = msgUnreadRecordService.getOne(queryWrapper);
        if (ObjectUtil.isNotNull(msgUnreadRecord)) {
            msgUnreadRecord.setUnreadNum(0);
            msgUnreadRecordService.update(msgUnreadRecord);
        }
        return convert;
    }

}
