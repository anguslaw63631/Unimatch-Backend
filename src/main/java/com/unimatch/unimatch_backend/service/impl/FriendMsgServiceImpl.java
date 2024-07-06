package com.unimatch.unimatch_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.common.enums.CommonStatusEnum;
import com.unimatch.unimatch_backend.common.enums.ServiceErrorEnum;
import com.unimatch.unimatch_backend.common.exception.ServiceException;
import com.unimatch.unimatch_backend.mapper.FriendMsgMapper;
import com.unimatch.unimatch_backend.model.entity.FriendMsg;
import com.unimatch.unimatch_backend.model.entity.MsgUnreadRecord;
import com.unimatch.unimatch_backend.model.vo.FriendMsgVo;
import com.unimatch.unimatch_backend.model.vo.MsgHandleVo;
import com.unimatch.unimatch_backend.service.FriendMsgService;
import com.unimatch.unimatch_backend.service.MsgUnreadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class FriendMsgServiceImpl extends ServiceImpl<FriendMsgMapper, FriendMsg> implements FriendMsgService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Resource
    private MsgUnreadRecordService msgUnreadRecordService;

    @Override
    public FriendMsgVo add(FriendMsgVo friendMsgVo) {
        // Query unread record
        LambdaQueryWrapper<MsgUnreadRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsgUnreadRecord::getUserId, friendMsgVo.getToUserId()).eq(MsgUnreadRecord::getTargetId, friendMsgVo.getFromUserId());
        MsgUnreadRecord msgUnreadRecord = msgUnreadRecordService.getOne(queryWrapper);
        if (ObjectUtil.isNull(msgUnreadRecord)) {
            msgUnreadRecord = new MsgUnreadRecord();
            msgUnreadRecord.setUnreadNum(1);
            msgUnreadRecord.setUserId(friendMsgVo.getToUserId());
            msgUnreadRecord.setTargetId(friendMsgVo.getFromUserId());
            msgUnreadRecord.setSource(CommonStatusEnum.MSG_SOURCE_FRIEND.getResultCode());
        } else {
            msgUnreadRecord.setUnreadNum(msgUnreadRecord.getUnreadNum() + 1);
        }
        msgUnreadRecordService.update(msgUnreadRecord);
        // vo->entity
        FriendMsg friendMsg = new FriendMsg();
        BeanUtil.copyProperties(friendMsgVo, friendMsg);
        if (baseMapper.insert(friendMsg) > 0) {
            // Fill missing fields
            friendMsgVo.setId(friendMsg.getId());
            friendMsgVo.setSource(CommonStatusEnum.MSG_SOURCE_FRIEND.getResultCode());
            messagingTemplate.convertAndSend("/simple/message/" + friendMsgVo.getToUserId(), JSONUtil.toJsonStr(friendMsgVo));
        }
        // entity->vo
        FriendMsgVo resFriendMsgVo = new FriendMsgVo();
        BeanUtil.copyProperties(friendMsg, resFriendMsgVo);
        return resFriendMsgVo;
    }

    @Override
    public IPage<FriendMsgVo> page(FriendMsgVo friendMsgVo, Integer current, Integer size) {
        IPage<FriendMsg> page = new Page<>(current, size);
        LambdaQueryWrapper<FriendMsg> query = new LambdaQueryWrapper<>();
        query.eq(FriendMsg::getFromUserId, friendMsgVo.getFromUserId()).eq(FriendMsg::getToUserId, friendMsgVo.getToUserId());
        query.or().eq(FriendMsg::getFromUserId, friendMsgVo.getToUserId()).eq(FriendMsg::getToUserId, friendMsgVo.getFromUserId());
        query.orderByDesc(FriendMsg::getCreateTime);
        IPage<FriendMsg> friendMsgs = this.page(page, query);
        // IPage<entity>->IPage<vo>
        IPage<FriendMsgVo> convert = friendMsgs.convert(FriendMsg -> BeanUtil.copyProperties(FriendMsg, FriendMsgVo.class));
        // Query unread record
        LambdaQueryWrapper<MsgUnreadRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsgUnreadRecord::getUserId, friendMsgVo.getFromUserId()).eq(MsgUnreadRecord::getTargetId, friendMsgVo.getToUserId());
        MsgUnreadRecord msgUnreadRecord = msgUnreadRecordService.getOne(queryWrapper);
        if (ObjectUtil.isNotNull(msgUnreadRecord)) {
            msgUnreadRecord.setUnreadNum(0);
            msgUnreadRecordService.update(msgUnreadRecord);
        }
        return convert;
    }

    @Override
    public void msgHandle(MsgHandleVo msgHandleVo) {
        byte type = msgHandleVo.getType();
        Long msgId = msgHandleVo.getId();
        FriendMsg friendMsg = this.getById(msgId);
        if (CommonStatusEnum.MSG_STATUS_REVOKE.getResultCode() == type) {
            // Revoke
            DateTime dateTime = DateUtil.offsetMinute(friendMsg.getCreateTime(), 3);
            if (dateTime.getTime() < DateUtil.current()) {
                throw new ServiceException(ServiceErrorEnum.MSG_REVOKE_TIMEOUT_ERROR);
            }
            friendMsg.setStatus(CommonStatusEnum.MSG_STATUS_REVOKE.getResultCode());
            friendMsg.setMsgType(CommonStatusEnum.MSG_TYPE_SYSTEM.getResultCode());
        } else if (CommonStatusEnum.MSG_STATUS_DELETE.getResultCode() == type) {
            // Delete
            friendMsg.setStatus(CommonStatusEnum.MSG_STATUS_DELETE.getResultCode());
            friendMsg.setMsgType(CommonStatusEnum.MSG_TYPE_SYSTEM.getResultCode());
        }
        messagingTemplate.convertAndSend("/msgHandle/message/" + friendMsg.getToUserId(), JSONUtil.toJsonStr(msgHandleVo));
        this.updateById(friendMsg);
    }

}
