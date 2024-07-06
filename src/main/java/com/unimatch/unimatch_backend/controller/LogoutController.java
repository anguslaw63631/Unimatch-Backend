package com.unimatch.unimatch_backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "Logout Controller")
@RequestMapping("/logout")
public class LogoutController {

    @ApiOperation(value = "Logout")
    @GetMapping
    public ResponseData logout() {
        //Clean Data
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return ResponseData.success("Logout successfully");
    }
}
