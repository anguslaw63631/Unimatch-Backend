package com.unimatch.unimatch_backend.controller;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.MessageVo;
import com.unimatch.unimatch_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "Message Controller")
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "Message List")
    @PostMapping
    public ResponseData list(MessageVo messageVo) {
        log.info("MsgData,messageVo = {}", JSONUtil.toJsonStr(messageVo));
        List<MessageVo> listMessageVo = messageService.list(messageVo);
        return ResponseData.success(listMessageVo);
    }

}
