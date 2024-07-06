package com.unimatch.unimatch_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unimatch.unimatch_backend.common.shiro.util.SubjectUtil;
import com.unimatch.unimatch_backend.mapper.UserMapper;
import com.unimatch.unimatch_backend.model.entity.User;
import com.unimatch.unimatch_backend.model.vo.UserVo;
import com.unimatch.unimatch_backend.service.FriendService;
import com.unimatch.unimatch_backend.service.MatchService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchServiceImpl extends ServiceImpl<UserMapper, User> implements MatchService {

    @Resource
    private FriendService friendService;
    @Override
    public IPage<UserVo> normalMatch(int current, int size) {
        IPage<User> page = new Page<>(current, size);
        Long userId = SubjectUtil.getUserId();
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper();
        lambdaQuery.isNotNull(User::getUsername);
        IPage<User> users = this.page(page, lambdaQuery);

        List<User> userList = users.getRecords();

        Map<Long, List<Short>> userData = new HashMap<>();

        for (User user : userList) {
            List<Short> interests = new ArrayList<>();
            interests.add(user.getInterest1());
            interests.add(user.getInterest2());
            interests.add(user.getInterest3());
            userData.put(user.getId(), interests);
            System.out.println("user:" + user.getUsername() + "interests:" + interests.toString());
        }

        List<Long> matchingFriends = findMatchingFriends(userData, userId);


        // IPage<entity>->IPage<vo>
        IPage<UserVo> convert = users.convert(user -> {
            UserVo resUserVo = new UserVo();
            if (!matchingFriends.contains(user.getId())) {
                return null;
            }
            if(friendService.isFriend(userId, String.valueOf(user.getId())) != null){
                return null;
            }
            BeanUtil.copyProperties(user, resUserVo);
            return resUserVo;
        });
        return convert;
    }

    @Override
    public IPage<UserVo> locationMatch(int current, int size, short locationCode) {
        IPage<User> page = new Page<>(current, size);
        Long userId = SubjectUtil.getUserId();
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper();
        lambdaQuery.isNotNull(User::getUsername).eq(User::getLast_location, locationCode);
        IPage<User> users = this.page(page, lambdaQuery);

        List<User> userList = users.getRecords();

        Map<Long, List<Short>> userData = new HashMap<>();

        for (User user : userList) {
            List<Short> interests = new ArrayList<>();
            interests.add(user.getInterest1());
            interests.add(user.getInterest2());
            interests.add(user.getInterest3());
            userData.put(user.getId(), interests);
            System.out.println("user:" + user.getUsername() + "interests:" + interests.toString());
        }

        List<Long> matchingFriends = findMatchingFriends(userData, userId);


        // IPage<entity>->IPage<vo>
        IPage<UserVo> convert = users.convert(user -> {
            UserVo resUserVo = new UserVo();
            if (!matchingFriends.contains(user.getId())) {
                return null;
            }
            BeanUtil.copyProperties(user, resUserVo);
            return resUserVo;
        });
        return convert;
    }

    public static List<Long> findMatchingFriends(Map<Long, List<Short>> userData, Long targetUser) {
        List<Long> matchingFriends = new ArrayList<>();

        // Get the interests of the target user
        List<Short> targetInterests = userData.get(targetUser);

        System.out.println("targetUser:" + targetUser + "interests:" + targetInterests.toString());

        // Iterate over each user in the data
        for (Map.Entry<Long, List<Short>> entry : userData.entrySet()) {
            Long user = entry.getKey();
            List<Short> interests = entry.getValue();

            // Skip the target user
            if (user.equals(targetUser)) {
                continue;
            }

            // Calculate similarity score using collaborative filtering
            double similarity = calculateSimilarity(targetInterests, interests);

            // Add user to matching friends if similarity score is above a threshold
            if (similarity >= 0.5) {
                matchingFriends.add(user);
            }
        }

        return matchingFriends;
    }

    public static double calculateSimilarity(List<Short> interests1, List<Short> interests2) {
        // Calculate the number of common interests
        int commonInterests = 0;
        for (Short interest : interests1) {
            if (interests2.contains(interest)) {
                commonInterests++;
            }
        }

        // Calculate similarity score as the ratio of common interests to total interests
        double similarity = (double) commonInterests / (double) interests1.size();

        return similarity;
    }
}
