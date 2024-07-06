package com.unimatch.unimatch_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.common.enums.CommonStatusEnum;
import com.unimatch.unimatch_backend.common.enums.ServiceErrorEnum;
import com.unimatch.unimatch_backend.common.exception.ServiceException;
import com.unimatch.unimatch_backend.common.shiro.util.SubjectUtil;
import com.unimatch.unimatch_backend.mapper.GroupMapper;
import com.unimatch.unimatch_backend.model.entity.Group;
import com.unimatch.unimatch_backend.model.entity.GroupMsg;
import com.unimatch.unimatch_backend.model.entity.GroupUser;
import com.unimatch.unimatch_backend.model.entity.User;
import com.unimatch.unimatch_backend.model.vo.GroupUserJoinVo;
import com.unimatch.unimatch_backend.model.vo.GroupUserVo;
import com.unimatch.unimatch_backend.model.vo.GroupVo;
import com.unimatch.unimatch_backend.model.vo.MsgHandleVo;
import com.unimatch.unimatch_backend.service.GroupMsgService;
import com.unimatch.unimatch_backend.service.GroupService;
import com.unimatch.unimatch_backend.service.GroupUserService;
import com.unimatch.unimatch_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private GroupMsgService groupMsgService;

    @Resource
    private UserService userService;

    @Override
    public GroupVo add(GroupVo groupVo) {
        User userInfo = SubjectUtil.getUserInfo();
        // vo->entity
        Group group = new Group();
        BeanUtil.copyProperties(groupVo, group);
        group.setStatus(CommonStatusEnum.YES.getResultCode());
        group.setAdminUserId(userInfo.getId());
        String firstWord = StrUtil.sub(group.getName(), 0, 1);
        String alphabetic = PinyinUtil.getFirstLetter(firstWord, ",");
        group.setAlphabetic(alphabetic.toUpperCase());
        this.save(group);
        this.buildGroupUser(group, userInfo);
        this.buildGroupMsg(group, userInfo);
        // entity->vo
        GroupVo resGroupVo = new GroupVo();
        BeanUtil.copyProperties(group, resGroupVo);
        return resGroupVo;
    }

    @Override
    public IPage<GroupVo> page(GroupUserVo groupUserVo, int current, int size) {
        IPage<GroupUser> page = new Page<>(current, size);
        Long userId = SubjectUtil.getUserId();
        LambdaQueryWrapper<GroupUser> lambdaQuery = new LambdaQueryWrapper();
        lambdaQuery.eq(GroupUser::getUserId, userId).like(ObjectUtil.isNotNull(groupUserVo.getGroupName()), GroupUser::getGroupName, groupUserVo.getGroupName()).eq(GroupUser::getStatus, CommonStatusEnum.YES.getResultCode());
        IPage<GroupUser> groupUsers = groupUserService.page(page, lambdaQuery);
        // IPage<entity>->IPage<vo>
        IPage<GroupVo> convert = groupUsers.convert(groupUser -> {
            Group group = this.getById(groupUser.getGroupId());
            GroupVo groupVo = new GroupVo();
            BeanUtil.copyProperties(group, groupVo);
            return groupVo;
        });
        return convert;
    }

    @Override
    public IPage<GroupVo> getAllGroupList(int current, int size) {
        IPage<GroupUser> page = new Page<>(current, size);
        Long userId = SubjectUtil.getUserId();
        LambdaQueryWrapper<GroupUser> lambdaQuery = new LambdaQueryWrapper();
        lambdaQuery.isNotNull(GroupUser::getGroupName);
        IPage<GroupUser> groupUsers = groupUserService.page(page, lambdaQuery);
        // IPage<entity>->IPage<vo>
        IPage<GroupVo> convert = groupUsers.convert(groupUser -> {
            Group group = this.getById(groupUser.getGroupId());
            GroupVo groupVo = new GroupVo();
            BeanUtil.copyProperties(group, groupVo);
            return groupVo;
        });
        return convert;
    }

    @Override
    public IPage<GroupVo> getGroupListByTag(int current, int size,short groupTag) {
        IPage<GroupUser> page = new Page<>(current, size);
        LambdaQueryWrapper<GroupUser> lambdaQuery = new LambdaQueryWrapper();
        lambdaQuery.isNotNull(GroupUser::getGroupName);
        IPage<GroupUser> groupUsers = groupUserService.page(page, lambdaQuery);
        // IPage<entity>->IPage<vo>
        IPage<GroupVo> convert = groupUsers.convert(groupUser -> {

            Group group = this.getById(groupUser.getGroupId());
            if(group.getTag() != groupTag){
                return null;
            }
                GroupVo groupVo = new GroupVo();
                BeanUtil.copyProperties(group, groupVo);
                return groupVo;
        });
        return convert;
    }

    @Override
    public GroupVo detail(String groupId) {
        Group group = this.getById(groupId);
        GroupVo groupVo = new GroupVo();
        BeanUtil.copyProperties(group, groupVo);
        LambdaQueryWrapper<GroupUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupUser::getGroupId, groupId);
        List<GroupUser> groupUsers = groupUserService.list(queryWrapper);
        List<GroupUserVo> resultGroupUsers = CollectionUtil.newArrayList();
        groupUsers.stream().forEach(item -> {
            GroupUserVo groupUserVo = new GroupUserVo();
            BeanUtil.copyProperties(item, groupUserVo);
            resultGroupUsers.add(groupUserVo);
        });
        groupVo.setGroupUsers(resultGroupUsers);
        return groupVo;
    }

    private void buildGroupUser(Group group, User userInfo) {
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group.getId());
        groupUser.setGroupName(group.getName());
        groupUser.setUserId(group.getAdminUserId());
        groupUser.setUserNickname(userInfo.getNickname() + "(Admin)");
        groupUser.setUserAvatar(userInfo.getAvatar());
        groupUser.setAdminable(CommonStatusEnum.YES.getResultCode());
        //Status 1: normal; 0: delete; -1 quit group
        groupUser.setStatus(CommonStatusEnum.YES.getResultCode());
        groupUserService.save(groupUser);
    }

    private void buildGroupMsg(Group group, User userInfo) {
        GroupMsg groupMsg = new GroupMsg();
        groupMsg.setGroupId(group.getId());
        groupMsg.setMsgType(CommonStatusEnum.MSG_TYPE_SYSTEM.getResultCode());
        groupMsg.setMsgContent("Welcome " + userInfo.getNickname() + " to join " + group.getName());
        groupMsg.setStatus(CommonStatusEnum.MSG_STATUS_NORMAL.getResultCode());
        groupMsg.setFromUserId(userInfo.getId());
        groupMsg.setFromUserAvatar(userInfo.getAvatar());
        groupMsg.setFromUserNickname(userInfo.getNickname());
        groupMsgService.save(groupMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pull(GroupUserJoinVo groupUserJoinVo) {
        String userIds = groupUserJoinVo.getUserIds();
        Long groupId = groupUserJoinVo.getGroupId();
        User userInfo = SubjectUtil.getUserInfo();
        List<String> listUserIds = Arrays.asList(userIds.split(","));
        Group group = this.getById(groupId);
        AtomicInteger num = new AtomicInteger();
        final String[] firstName = {""};
        listUserIds.forEach(userId -> {
            GroupUser newGroupUser = new GroupUser();
            User user = userService.getById(Long.parseLong(userId));
            // If the user is already in the group, then skip
            LambdaQueryWrapper<GroupUser> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(GroupUser::getGroupId, groupId).eq(GroupUser::getUserId, userId);
            GroupUser groupUser = groupUserService.getOne(queryWrapper);
            if (ObjectUtil.isNull(groupUser)) {
                newGroupUser.setUserId(user.getId());
                newGroupUser.setGroupId(groupId);
                newGroupUser.setGroupName(group.getName());
                newGroupUser.setUserAvatar(user.getAvatar());
                newGroupUser.setUserNickname(user.getNickname());
                newGroupUser.setStatus(CommonStatusEnum.YES.getResultCode());
                newGroupUser.setAdminable(CommonStatusEnum.NO.getResultCode());
                newGroupUser.setSource(CommonStatusEnum.GROUP_USER_PULL.getResultCode());
                groupUserService.save(newGroupUser);
                num.getAndIncrement();
                if (StrUtil.isBlank(firstName[0])) {
                    firstName[0] = user.getNickname();
                }
            }
        });
        GroupMsg groupMsg = new GroupMsg();
        groupMsg.setGroupId(groupId);
        String tpl = "Welcome {},{} to join "+group.getName();
        groupMsg.setMsgContent(StrUtil.format(tpl, firstName, num));
        groupMsg.setMsgType(CommonStatusEnum.MSG_TYPE_SYSTEM.getResultCode());
        groupMsg.setFromUserId(userInfo.getId());
        groupMsg.setFromUserAvatar(userInfo.getAvatar());
        groupMsg.setFromUserNickname(userInfo.getNickname());
        groupMsg.setStatus(CommonStatusEnum.MSG_STATUS_NORMAL.getResultCode());
        groupMsgService.save(groupMsg);
    }

    @Override
    public void join(GroupUserJoinVo groupUserJoinVo) {
        String userId = groupUserJoinVo.getUserIds();
        Long groupId = groupUserJoinVo.getGroupId();
        Group group = this.getById(groupId);
        User user = userService.getById(Long.parseLong(userId));
        GroupUser newGroupUser = new GroupUser();
        newGroupUser.setUserId(Long.parseLong(userId));
        newGroupUser.setGroupId(groupId);
        newGroupUser.setGroupName(group.getName());
        newGroupUser.setUserAvatar(user.getAvatar());
        newGroupUser.setUserNickname(user.getNickname());
        newGroupUser.setStatus(CommonStatusEnum.YES.getResultCode());
        newGroupUser.setAdminable(CommonStatusEnum.NO.getResultCode());
        newGroupUser.setSource(CommonStatusEnum.GROUP_USER_SCAN.getResultCode());
        groupUserService.save(newGroupUser);
    }

    @Override
    public void msgHandle(MsgHandleVo msgHandleVo) {
        byte type = msgHandleVo.getType();
        Long msgId = msgHandleVo.getId();
        GroupMsg groupMsg = groupMsgService.getById(msgId);
        if (CommonStatusEnum.MSG_STATUS_REVOKE.getResultCode() == type) {
            // Revoke
            DateTime dateTime = DateUtil.offsetMinute(groupMsg.getCreateTime(), 3);
            if (dateTime.getTime() < DateUtil.current()) {
                throw new ServiceException(ServiceErrorEnum.MSG_REVOKE_TIMEOUT_ERROR);
            }
            groupMsg.setStatus(CommonStatusEnum.MSG_STATUS_REVOKE.getResultCode());
            groupMsg.setMsgType(CommonStatusEnum.MSG_TYPE_SYSTEM.getResultCode());
        } else if (CommonStatusEnum.MSG_STATUS_DELETE.getResultCode() == type) {
            // Delete
            groupMsg.setStatus(CommonStatusEnum.MSG_STATUS_DELETE.getResultCode());
            groupMsg.setMsgType(CommonStatusEnum.MSG_TYPE_SYSTEM.getResultCode());
        }
        groupMsgService.updateById(groupMsg);
        messagingTemplate.convertAndSend("/msgHandle/message/" + groupMsg.getGroupId(), JSONUtil.toJsonStr(msgHandleVo));
    }
}
