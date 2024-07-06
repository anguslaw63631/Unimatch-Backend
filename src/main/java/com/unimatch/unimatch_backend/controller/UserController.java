package com.unimatch.unimatch_backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.UserPwdVo;
import com.unimatch.unimatch_backend.model.vo.UserVo;
import com.unimatch.unimatch_backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(tags = "User Controller")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "User Detail")
    @GetMapping("/{userId}")
    public ResponseData detail(@PathVariable Long userId) {
        return ResponseData.success(userService.getById(userId));
    }

    @ApiOperation(value = "Update User")
    @PutMapping
    public ResponseData update(final @RequestBody @Valid UserVo userVo) {
        if (userService.update(userVo)) {
            return ResponseData.success();
        }
        return ResponseData.error("Update failed");
    }

    @ApiOperation(value = "Update Password")
    @PutMapping("/pwd")
    public ResponseData pwd(final @RequestBody @Valid UserPwdVo userPwdVo) {
        if (userService.pwd(userPwdVo)) {
            return ResponseData.success();
        }
        return ResponseData.error("Update failed");
    }

    @ApiOperation(value = "User List")
    @GetMapping
    public ResponseData get(final UserVo UserVo,
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        IPage<UserVo> page = userService.page(UserVo, current, size);
        return ResponseData.success(page);
    }
}
