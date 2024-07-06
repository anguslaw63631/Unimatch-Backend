package com.unimatch.unimatch_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.common.enums.CommonStatusEnum;
import com.unimatch.unimatch_backend.common.enums.ServiceErrorEnum;
import com.unimatch.unimatch_backend.common.exception.ServiceException;
import com.unimatch.unimatch_backend.common.shiro.util.JwtUtil;
import com.unimatch.unimatch_backend.common.shiro.util.SubjectUtil;
import com.unimatch.unimatch_backend.common.util.EncryptUtil;
import com.unimatch.unimatch_backend.mapper.UserMapper;
import com.unimatch.unimatch_backend.model.entity.User;
import com.unimatch.unimatch_backend.model.vo.*;
import com.unimatch.unimatch_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Override
    public Boolean register(RegisterVo registerVo) {
        // Check if the user/email already exists
        User one = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, registerVo.getUsername()));
        if (null != one) {
            throw new ServiceException(ServiceErrorEnum.USER_IS_EXIT);
        }
        // vo->entity
        User user = new User();
        BeanUtil.copyProperties(registerVo, user);

        String pwd = IdUtil.simpleUUID().substring(0, 6);

        //test
//        pwd = "123456";
        sendVerifyMail(registerVo.getUsername() + "@connect.ust.hk",pwd);
        String password = EncryptUtil.encrypt(pwd);
        user.setPassword(password);
        // default nickname = email prefix
        user.setNickname(registerVo.getUsername());
        user.setEmail(registerVo.getUsername() + "@connect.ust.hk");
        // random avatar https://api.multiavatar.com/xxx.png
        String avatar = "http://api.multiavatar.com/" + (int) (Math.random() * 100) + ".png";
        user.setAvatar(avatar);
        user.setSex("0");
        user.setIntro("This user wrote nothing.");
        return this.save(user);
    }

    @Override
    public UserVo login(LoginVo loginVo) {
        User one = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, loginVo.getUsername()).eq(User::getStatus, CommonStatusEnum.YES.getResultCode()));
        if (null == one) {
            throw new ServiceException(ServiceErrorEnum.USER_IS_NO_EXIT);
        }
        if (!loginVo.getPassword().equals(EncryptUtil.decrypt(one.getPassword()))) {
            throw new ServiceException(ServiceErrorEnum.PASSWORD_IS_ERROR);
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(one, userVo);
        userVo.setToken(JwtUtil.createToken(loginVo.getUsername()));
        return userVo;
    }

    @Override
    public Boolean update(UserVo userVo) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.set(User::getAvatar, userVo.getAvatar())
                .set(User::getSex, userVo.getSex())
                .set(User::getNickname, userVo.getNickname())
                .set(User::getBirthday, userVo.getBirthday())
                .set(User::getIntro, userVo.getIntro())
                .set(User::getEmail, userVo.getEmail())
                .set(User::getMajor, userVo.getMajor())
                .set(User::getInterest1, userVo.getInterest1())
                .set(User::getInterest2, userVo.getInterest2())
                .set(User::getInterest3, userVo.getInterest3())
                .set(User::getLast_location, userVo.getLast_location())
                .eq(User::getId, userVo.getId());
        return this.update(updateWrapper);
    }

    @Override
    public Boolean retrieve(RetrieveVo retrieveVo) {
        String username = retrieveVo.getUsername();
        LambdaUpdateWrapper<User> queryWrapper = new LambdaUpdateWrapper();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException(ServiceErrorEnum.USER_IS_NO_EXIT);
        }
        String pwd = IdUtil.simpleUUID().substring(0, 6);
        String password = EncryptUtil.encrypt(pwd);
        user.setPassword(password);
        if (this.saveOrUpdate(user)) {
            this.sendResetPasswordMail(username+"@connect.ust.hk", pwd);
        }
        return true;
    }

    @Override
    public Boolean pwd(UserPwdVo userPwdVo) {
        Long userId = SubjectUtil.getUserId();
        User user = this.getById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException(ServiceErrorEnum.USER_IS_NO_EXIT);
        }
        String password = EncryptUtil.encrypt(userPwdVo.getOldPwd());
        if (!StrUtil.equals(password, user.getPassword())) {
            throw new ServiceException(ServiceErrorEnum.USER_OLD_PASSWORD_ERROR);
        }
        String newPwd = EncryptUtil.encrypt(userPwdVo.getNewPwd());
        user.setPassword(newPwd);
        return this.saveOrUpdate(user);
    }

    @Override
    public IPage<UserVo> page(UserVo userVo, int current, int size) {
        IPage<User> page = new Page<>(current, size);
        Long userId = SubjectUtil.getUserId();
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper();
        lambdaQuery.like(User::getUsername, userVo.getUsername()).eq(User::getStatus, CommonStatusEnum.YES.getResultCode()).ne(User::getUsername, SubjectUtil.getUsername());
        IPage<User> users = this.page(page, lambdaQuery);
        // IPage<entity>->IPage<vo>
        IPage<UserVo> convert = users.convert(user -> {
            UserVo resUserVo = new UserVo();
            BeanUtil.copyProperties(user, resUserVo);
            return resUserVo;
        });
        return convert;
    }

    @Override
    public IPage<UserVo> getAllActiveUser(UserVo userVo, int current, int size) {
        IPage<User> page = new Page<>(current, size);
        Long userId = SubjectUtil.getUserId();
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper();
        lambdaQuery.isNotNull(User::getUsername);
        IPage<User> users = this.page(page, lambdaQuery);
        // IPage<entity>->IPage<vo>
        IPage<UserVo> convert = users.convert(user -> {
            UserVo resUserVo = new UserVo();
            BeanUtil.copyProperties(user, resUserVo);
            return resUserVo;
        });
        return convert;
    }

    private void sendResetPasswordMail(String email, String password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("UniMatch Password Reset");
        mailMessage.setText("Your New Password:" + password);
        mailMessage.setTo(email);
        mailMessage.setFrom("UniMatchTeam@unimatch.social");
        javaMailSender.send(mailMessage);
    }

    private void sendVerifyMail(String email, String password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Welcome to UniMatch");
        mailMessage.setText("Welcome to UniMatch, Here is the password for first login:" + password);
        mailMessage.setTo(email);
        mailMessage.setFrom("UniMatchTeam@unimatch.social");
        javaMailSender.send(mailMessage);
    }

}
