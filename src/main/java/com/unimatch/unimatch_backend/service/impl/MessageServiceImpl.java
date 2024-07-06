package com.unimatch.unimatch_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.common.shiro.util.SubjectUtil;
import com.unimatch.unimatch_backend.mapper.MessageMapper;
import com.unimatch.unimatch_backend.model.entity.Message;
import com.unimatch.unimatch_backend.model.entity.User;
import com.unimatch.unimatch_backend.model.vo.MessageVo;
import com.unimatch.unimatch_backend.service.MessageService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<MessageVo> list(MessageVo messageVo) {
        User userInfo = SubjectUtil.getUserInfo();
        // vo->entity
        Message message = new Message();
        BeanUtil.copyProperties(messageVo, message);
        message.setMineUserId(userInfo.getId());
        List<Message> messages = baseMapper.list(message);
        List<MessageVo> messagesVo = new ArrayList<>();
        // entity->vo
        messages.forEach(item -> {
            MessageVo msgVo = new MessageVo();
            BeanUtil.copyProperties(item, msgVo);
            messagesVo.add(msgVo);
        });
        return messagesVo;
    }
}
