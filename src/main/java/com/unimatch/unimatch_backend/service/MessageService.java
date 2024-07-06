package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.Message;
import com.unimatch.unimatch_backend.model.vo.MessageVo;
import java.util.List;


public interface MessageService extends IService<Message> {

    //message list
    List<MessageVo> list(MessageVo messageVo);

}
