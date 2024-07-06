package com.unimatch.unimatch_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.mapper.GroupUserMapper;
import com.unimatch.unimatch_backend.model.entity.GroupUser;
import com.unimatch.unimatch_backend.service.GroupUserService;
import org.springframework.stereotype.Service;


@Service
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser> implements GroupUserService {

}
