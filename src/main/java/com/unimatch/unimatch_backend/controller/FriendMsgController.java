package com.unimatch.unimatch_backend.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.FriendMsgVo;
import com.unimatch.unimatch_backend.model.vo.MsgHandleVo;
import com.unimatch.unimatch_backend.service.FriendMsgService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@RestController
@Slf4j
@Api(tags = "Friend Message Controller")
@RequestMapping("friendMsg")
public class FriendMsgController {

    @Resource
    private FriendMsgService friendMsgService;

    @ApiOperation(value = "Send Friend Message")
    @PostMapping
    public ResponseData send(@RequestBody FriendMsgVo friendMsgVo) {
        log.info("MsgData,friendMsgVo = {}", JSONUtil.toJsonStr(friendMsgVo));
        FriendMsgVo resFriendMsgVo = friendMsgService.add(friendMsgVo);
        return ResponseData.success(resFriendMsgVo);
    }

    @ApiOperation(value = "Friend Message List")
    @GetMapping
    public ResponseData get(final FriendMsgVo friendMsgVo,
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        IPage<FriendMsgVo> page = friendMsgService.page(friendMsgVo, current, size);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "Revoke Friend Message")
    @PutMapping("/msgHandle")
    public ResponseData revoke(@RequestBody MsgHandleVo msgHandleVo) {
        friendMsgService.msgHandle(msgHandleVo);
        return ResponseData.success();
    }
}
