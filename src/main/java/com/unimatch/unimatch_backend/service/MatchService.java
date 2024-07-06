package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.User;
import com.unimatch.unimatch_backend.model.vo.UserVo;

public interface MatchService extends IService<User> {

    IPage<UserVo> normalMatch(int current, int size);

    IPage<UserVo> locationMatch( int current, int size,short locationCode);
}
