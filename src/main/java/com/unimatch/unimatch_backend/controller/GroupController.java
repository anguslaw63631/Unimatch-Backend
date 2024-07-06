package com.unimatch.unimatch_backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.GroupUserJoinVo;
import com.unimatch.unimatch_backend.model.vo.GroupUserVo;
import com.unimatch.unimatch_backend.model.vo.GroupVo;
import com.unimatch.unimatch_backend.model.vo.MsgHandleVo;
import com.unimatch.unimatch_backend.service.GroupService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@Api(tags = "Group Controller")
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    @ApiOperation(value = "Add Group")
    @PostMapping
    public ResponseData add(final @RequestBody @Valid GroupVo groupVo) {
        GroupVo resGroupVo = groupService.add(groupVo);
        return ResponseData.success(resGroupVo);
    }

    @ApiOperation(value = "Group List")
    @GetMapping
    public ResponseData get(final GroupUserVo groupUserVo,
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        IPage<GroupVo> page = groupService.page(groupUserVo, current, size);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "Group Detail")
    @GetMapping("/{groupId}")
    public ResponseData detail(@PathVariable String groupId) {
        GroupVo groupVo = groupService.detail(groupId);
        return ResponseData.success(groupVo);
    }

    @ApiOperation(value = "Invite Group", notes = "Invite User to a Group")
    @PostMapping("/pull")
    public ResponseData pull(@RequestBody GroupUserJoinVo groupUserJoinVo) {
        groupService.pull(groupUserJoinVo);
        return ResponseData.success();
    }

    @ApiOperation(value = "Join Group", notes = "Join Group by User")
    @PostMapping("/join")
    public ResponseData join(@RequestBody GroupUserJoinVo groupUserJoinVo) {
        groupService.join(groupUserJoinVo);
        return ResponseData.success();
    }

    @ApiOperation(value = "Revoke Group Message")
    @PutMapping("/msgHandle")
    public ResponseData msgHandle(@RequestBody MsgHandleVo msgHandleVo) {
        groupService.msgHandle(msgHandleVo);
        return ResponseData.success();
    }
}
