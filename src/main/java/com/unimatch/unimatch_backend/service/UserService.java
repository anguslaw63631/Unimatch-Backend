package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.User;
import com.unimatch.unimatch_backend.model.vo.*;


public interface UserService extends IService<User> {


    Boolean register(RegisterVo registerVo);


    UserVo login(LoginVo loginVo);

    //update user detail
    Boolean update(UserVo userVo);

   //retrieve password
    Boolean retrieve(RetrieveVo retrieveVo);

    //update password
    Boolean pwd(UserPwdVo userPwdVo);

    //user list
    IPage<UserVo> page(UserVo userVo, int current, int size);

    IPage<UserVo> getAllActiveUser(UserVo userVo, int current, int size);

}
