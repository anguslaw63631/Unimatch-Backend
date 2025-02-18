package com.unimatch.unimatch_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.common.enums.CommonErrorEnum;
import com.unimatch.unimatch_backend.common.enums.CommonStatusEnum;
import com.unimatch.unimatch_backend.common.enums.InvitationStatusEnum;
import com.unimatch.unimatch_backend.common.enums.ServiceErrorEnum;
import com.unimatch.unimatch_backend.common.exception.ServiceException;
import com.unimatch.unimatch_backend.common.shiro.util.SubjectUtil;
import com.unimatch.unimatch_backend.mapper.InvitationMapper;
import com.unimatch.unimatch_backend.model.entity.Friend;
import com.unimatch.unimatch_backend.model.entity.FriendMsg;
import com.unimatch.unimatch_backend.model.entity.Invitation;
import com.unimatch.unimatch_backend.model.entity.User;
import com.unimatch.unimatch_backend.model.vo.InvitationVo;
import com.unimatch.unimatch_backend.service.FriendMsgService;
import com.unimatch.unimatch_backend.service.FriendService;
import com.unimatch.unimatch_backend.service.InvitationService;
import com.unimatch.unimatch_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;


@Service
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {

    @Resource
    private UserService userService;

    @Resource
    private FriendService friendService;

    @Resource
    private FriendMsgService friendMsgService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    @Override
    public InvitationVo add(InvitationVo friendInvitationVo) {
        Long userId = SubjectUtil.getUserId();
        Long friendId = friendInvitationVo.getFriendId();
        User user = userService.getById(friendId);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException(ServiceErrorEnum.USER_IS_NO_EXIT);
        }
        // Check if the user is already a friend
        LambdaQueryWrapper<Friend> friendWrapper = new LambdaQueryWrapper<>();
        friendWrapper.eq(Friend::getUserId,userId).eq(Friend::getFriendId, friendId).eq(Friend::getStatus, CommonStatusEnum.YES.getResultCode());
        Friend friend = friendService.getOne(friendWrapper);
        if (ObjectUtil.isNotNull(friend)) {
            throw new ServiceException(ServiceErrorEnum.FRIEND_ALREADY_ERROR);
        }
        // Check if the user has already sent an invitation
        LambdaQueryWrapper<Invitation> invitationWrapper = new LambdaQueryWrapper<>();
        invitationWrapper.eq(Invitation::getUserId, userId).eq(Invitation::getFriendId, friendId).eq(Invitation::getStatus, CommonStatusEnum.INVITATION_WAIT.getResultCode());
        Invitation dbInvitation = this.getOne(invitationWrapper);
        if (ObjectUtil.isNotNull(dbInvitation)) {
            throw new ServiceException(ServiceErrorEnum.INVITATION_ALREADY_ERROR);
        }
        Invitation friendInvitation = new Invitation();
        friendInvitation.setUserId(userId);
        friendInvitation.setFriendId(friendId);
        friendInvitation.setFriendNickname(user.getNickname());
        friendInvitation.setUserNickname(SubjectUtil.getUserInfo().getNickname());
        friendInvitation.setUserAvatar(SubjectUtil.getAvatar());
        friendInvitation.setFriendAvatar(user.getAvatar());
        if (!this.save(friendInvitation)) {
            throw new ServiceException(CommonErrorEnum.SAVE_ERROR);
        }
        InvitationVo resFriendInvitationVo = new InvitationVo();
        BeanUtil.copyProperties(friendInvitation, resFriendInvitationVo);
        LambdaQueryWrapper<Invitation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Invitation::getFriendId, friendId).eq(Invitation::getStatus, CommonStatusEnum.INVITATION_WAIT.getResultCode());
        List<Invitation> list = this.list(queryWrapper);
        // Send invitation notification
        messagingTemplate.convertAndSend("/invitation/message/" + friendId, list.size());
        return resFriendInvitationVo;
    }

    @Override
    public void update(InvitationVo friendInvitationVo) {
        Long userId = SubjectUtil.getUserId();
        Long friendId = friendInvitationVo.getFriendId();
        User user = userService.getById(friendId);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException(ServiceErrorEnum.USER_IS_NO_EXIT);
        }
        LambdaQueryWrapper<Invitation> query = new LambdaQueryWrapper();
        query.eq(ObjectUtil.isNotNull(userId), Invitation::getUserId, userId);
        query.eq(ObjectUtil.isNotNull(friendId), Invitation::getFriendId, friendId);
        // obtain friend data
        Invitation friendInvitation = this.getOne(query);
        friendInvitation.setStatus(InvitationStatusEnum.WAIT.getResultCode());
        friendInvitation.setRemark(friendInvitationVo.getRemark());
        // set invitation data
        LambdaQueryWrapper<Invitation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Invitation::getFriendId, friendId).eq(Invitation::getStatus, CommonStatusEnum.INVITATION_WAIT.getResultCode()).or().eq(Invitation::getStatus, CommonStatusEnum.INVITATION_REFUSE.getResultCode());
        List<Invitation> list = this.list(queryWrapper);
        // Send invitation notification
        messagingTemplate.convertAndSend("/invitation/message/" + friendId, list.size());
        this.updateById(friendInvitation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(InvitationVo invitationVo) {
        Invitation invitation = this.getById(invitationVo.getId());
        byte status = invitationVo.getStatus();
        invitation.setRemark(invitationVo.getRemark());
        invitation.setReason(invitationVo.getReason());
        invitation.setStatus(status);
        if (CommonStatusEnum.INVITATION_PASS.getResultCode() == status && this.updateById(invitation)) {
            buildFriend(invitation);
            buildFriendMsg(invitation);
        } else {
            this.updateById(invitation);
        }
        LambdaQueryWrapper<Invitation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Invitation::getFriendId, invitation.getFriendId()).eq(Invitation::getStatus, CommonStatusEnum.INVITATION_WAIT.getResultCode());
        List<Invitation> list = this.list(queryWrapper);
        // Send invitation notification
        messagingTemplate.convertAndSend("/invitation/message/" + invitationVo.getFriendId(), list.size());
    }

    private void buildFriend(Invitation invitation) {
        Long friendId = invitation.getFriendId();
        Long userId = invitation.getUserId();
        String friendNickname = invitation.getFriendNickname();
        String userNickname = invitation.getUserNickname();
        Friend user = new Friend();
        user.setUserId(userId);
        user.setFriendId(friendId);
        user.setNickname(friendNickname);
        String userFirstWord = StrUtil.sub(friendNickname, 0, 1);
        String userAlphabetic = PinyinUtil.getFirstLetter(userFirstWord, ",");
        user.setAlphabetic(userAlphabetic.toUpperCase());
        user.setStatus(CommonStatusEnum.YES.getResultCode());
        User dbUser = userService.getById(friendId);
        user.setAvatar(dbUser.getAvatar());
        friendService.save(user);
        Friend friend = new Friend();
        friend.setUserId(friendId);
        friend.setFriendId(userId);
        friend.setNickname(userNickname);
        String friendFirstWord = StrUtil.sub(userNickname, 0, 1);
        String friendAlphabetic = PinyinUtil.getFirstLetter(friendFirstWord, ",");
        friend.setAlphabetic(friendAlphabetic.toUpperCase());
        friend.setStatus(CommonStatusEnum.YES.getResultCode());
        User dbFriend = userService.getById(userId);
        friend.setAvatar(dbFriend.getAvatar());
        friendService.save(friend);
    }

    private void buildFriendMsg(Invitation invitation) {
        FriendMsg sysMsg = new FriendMsg();
        sysMsg.setStatus(CommonStatusEnum.MSG_STATUS_NORMAL.getResultCode());
        sysMsg.setMsgContent(invitation.getFriendNickname() + " Accepted your friend request");
        sysMsg.setMsgType(CommonStatusEnum.MSG_TYPE_SYSTEM.getResultCode());
        sysMsg.setFromUserId(invitation.getUserId());
        sysMsg.setToUserId(invitation.getFriendId());
        friendMsgService.save(sysMsg);
        FriendMsg friendMsg = new FriendMsg();
        friendMsg.setStatus(CommonStatusEnum.MSG_STATUS_NORMAL.getResultCode());
        friendMsg.setMsgContent("We are friends now, begin to chat");
        friendMsg.setMsgType(CommonStatusEnum.MSG_TYPE_TEXT.getResultCode());
        friendMsg.setFromUserId(invitation.getUserId());
        friendMsg.setToUserId(invitation.getFriendId());
        friendMsgService.save(friendMsg);
    }

    @Override
    public List<InvitationVo> list(InvitationVo friendInvitationVo) {
        Long userId = friendInvitationVo.getUserId();
        Long friendId = friendInvitationVo.getFriendId();
        LambdaQueryWrapper<Invitation> query = new LambdaQueryWrapper();
        query.eq(ObjectUtil.isNotNull(userId), Invitation::getUserId, userId);
        query.eq(ObjectUtil.isNotNull(friendId), Invitation::getFriendId, friendId);
        query.orderByDesc(Invitation::getCreateTime);
        List<Invitation> invitations = this.list(query);
        List<InvitationVo> invitationsVo = CollectionUtil.newArrayList();
        invitations.forEach(invitation -> {
            InvitationVo invitationVo = new InvitationVo();
            BeanUtil.copyProperties(invitation, invitationVo);
            invitationsVo.add(invitationVo);
        });
        return invitationsVo;
    }
}
