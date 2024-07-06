package com.unimatch.unimatch_backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.InvitationVo;
import com.unimatch.unimatch_backend.service.InvitationService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;



@RestController
@Api(tags = "Friend Invitation Controller")
@RequestMapping("/invitation")
public class InvitationController {

    @Resource
    private InvitationService friendInvitationService;

    @ApiOperation(value = "Send Friend Invite")
    @PostMapping
    public ResponseData add(final @RequestBody InvitationVo friendInvitationVo) {
        InvitationVo resFriendInvitationVo = friendInvitationService.add(friendInvitationVo);
        return ResponseData.success(resFriendInvitationVo);
    }

    @ApiOperation(value = "Resend Friend Invite")
    @PutMapping
    public ResponseData update(final @RequestBody InvitationVo friendInvitationVo) {
        friendInvitationService.update(friendInvitationVo);
        return ResponseData.success();
    }

    @ApiOperation(value = "Friend Invitation Handler", notes = "Accept or reject friend invitation")
    @PutMapping("/handle")
    public ResponseData handle(final @RequestBody InvitationVo friendInvitationVo) {
        friendInvitationService.handle(friendInvitationVo);
        return ResponseData.success();
    }

    @ApiOperation(value = "Friend Invitation List")
    @GetMapping
    public ResponseData get(final InvitationVo friendInvitationVo) {
        List<InvitationVo> friendInvitations = friendInvitationService.list(friendInvitationVo);
        return ResponseData.success(friendInvitations);
    }

}
