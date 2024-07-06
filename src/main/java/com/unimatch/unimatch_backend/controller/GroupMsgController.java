package com.unimatch.unimatch_backend.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.GroupMsgVo;
import com.unimatch.unimatch_backend.service.GroupMsgService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@RestController
@Slf4j
@Api(tags = "Group Message Controller")
@RequestMapping("groupMsg")
public class GroupMsgController {

    @Resource
    private GroupMsgService groupMsgService;

    @ApiOperation(value = "Send Group Message")
    @PostMapping
    public ResponseData send(@RequestBody GroupMsgVo groupMsgVo) {
        log.info("MsgData,groupMsgVo = {}", JSONUtil.toJsonStr(groupMsgVo));
        GroupMsgVo resGroupMsgVo = groupMsgService.add(groupMsgVo);
        return ResponseData.success(resGroupMsgVo);
    }

    @ApiOperation(value = "Group Message List")
    @GetMapping
    public ResponseData get(final GroupMsgVo groupMsgVo,
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        IPage<GroupMsgVo> page = groupMsgService.page(groupMsgVo, current, size);
        return ResponseData.success(page);
    }

}
