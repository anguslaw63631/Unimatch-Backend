package com.unimatch.unimatch_backend.controller;

import com.unimatch.unimatch_backend.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Ibeacon Controller")
@RequestMapping("/ibeacon")
public class IbeaconController {
    @ApiOperation(value = "Ibeacon Data")
    @GetMapping("/{ibeaconId}")
    public ResponseData data(@PathVariable Long ibeaconId){
        String advertisedServiceUuid = "9e690835-c3fd-462a-aa77-6240ddfadb20";
        if(ibeaconId == 2){
            advertisedServiceUuid = "89a2ef2f-a624-42f8-9aac-be7e6d057852";
        }
        return ResponseData.success(advertisedServiceUuid);
    }
}
