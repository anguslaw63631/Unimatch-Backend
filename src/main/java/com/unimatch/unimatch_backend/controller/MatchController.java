package com.unimatch.unimatch_backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import com.unimatch.unimatch_backend.model.vo.GroupVo;
import com.unimatch.unimatch_backend.model.vo.UserVo;
import com.unimatch.unimatch_backend.service.GroupService;
import com.unimatch.unimatch_backend.service.MatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@Api(tags = "Match Controller")
@RequestMapping("/match")
public class MatchController {

    @Resource
    private MatchService matchService;

    @Resource
    private GroupService groupService;
    @ApiOperation(value = "Get Match List(Normal)")
    @GetMapping("/normal")
    public ResponseData getNormalMatchList(
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "50") Integer size) {
        IPage<UserVo> page = matchService.normalMatch(current, size);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "Get Match List(Location)")
    @GetMapping("/location")
    public ResponseData getLocationMatchList(
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "50") Integer size,
                                             final @RequestParam(value = "locationCode", required = true) Short locationCode) {
        IPage<UserVo> page = matchService.locationMatch(current, size,locationCode);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "Get All Group List")
    @GetMapping("/group")
    public ResponseData getAllGroupList(
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "50") Integer size) {
        IPage<GroupVo> page = groupService.getAllGroupList(current, size);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "Get Group List By Tag")
    @GetMapping("/group/{groupTag}")
    public ResponseData getGroupListByTag(
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "50") Integer size,
                                          @PathVariable Short groupTag) {
        IPage<GroupVo> page = groupService.getGroupListByTag(current, size, groupTag);
        return ResponseData.success(page);
    }
}
