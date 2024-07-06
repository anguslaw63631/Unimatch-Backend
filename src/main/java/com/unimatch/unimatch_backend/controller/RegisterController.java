package com.unimatch.unimatch_backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.RegisterVo;
import com.unimatch.unimatch_backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "Register Controller")
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "Register")
    @PostMapping
    public ResponseData register(@RequestBody @Valid RegisterVo registerVo) {

        userService.register(registerVo);
        return ResponseData.success();
    }
}
