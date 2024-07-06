package com.unimatch.unimatch_backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.FriendVo;
import com.unimatch.unimatch_backend.service.FriendService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@Api(tags = "Friend Controller")
@RequestMapping("/friend")
public class FriendController {

    @Resource
    private FriendService friendService;

    @ApiOperation(value = "Add Friend")
    @PostMapping
    public ResponseData add(final @RequestBody @Valid FriendVo friendVo) {
        FriendVo resFriendVo = friendService.add(friendVo);
        return ResponseData.success(resFriendVo);
    }

    @ApiOperation(value = "Friend List")
    @GetMapping
    public ResponseData get(final FriendVo friendVo,
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        IPage<FriendVo> page = friendService.page(friendVo, current, size);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "Friend Detail")
    @GetMapping("/{friendId}")
    public ResponseData detail(@PathVariable String friendId) {
        FriendVo friendVo = friendService.detail(friendId);
        return ResponseData.success(friendVo);
    }

    @ApiOperation(value = "Update Friend")
    @PutMapping
    public ResponseData update(final @RequestBody @Valid FriendVo friendVo) {
        if (friendService.update(friendVo)) {
            return ResponseData.success();
        }
        return ResponseData.error("Update failed");
    }

    @ApiOperation(value = "Delete Friend")
    @DeleteMapping("/{id}")
    public ResponseData delete(@PathVariable String id) {
        if (friendService.delete(id)) {
            return ResponseData.success();
        }
        return ResponseData.error("Delete failed");
    }
}
