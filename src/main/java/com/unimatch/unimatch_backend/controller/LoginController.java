package com.unimatch.unimatch_backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.LoginVo;
import com.unimatch.unimatch_backend.model.vo.RetrieveVo;
import com.unimatch.unimatch_backend.model.vo.UserVo;
import com.unimatch.unimatch_backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@Api(tags = "Login Controller")
@RequestMapping("/login")
public class LoginController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "Login")
    @PostMapping
    public ResponseData login(@RequestBody @Valid LoginVo loginVo) {
        UserVo userVo = userService.login(loginVo);
        return ResponseData.success(userVo);
    }

    @ApiOperation(value = "Retrieve Password")
    @PostMapping("/retrieve")
    public ResponseData retrieve(@RequestBody @Valid RetrieveVo retrieveVo) {
        if (userService.retrieve(retrieveVo)) {
            return ResponseData.success();
        }
        return ResponseData.error("Operation Failed");
    }
}
