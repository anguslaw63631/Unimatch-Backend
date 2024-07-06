package com.unimatch.unimatch_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unimatch.unimatch_backend.model.entity.Invitation;
import com.unimatch.unimatch_backend.model.vo.InvitationVo;
import java.util.List;


public interface InvitationService extends IService<Invitation> {

    //make a friend invitation
    InvitationVo add(InvitationVo friendInvitationVo);

    //update a friend invitation
    void update(InvitationVo friendInvitationVo);

    //accept/decline a friend invitation
    void handle(InvitationVo friendInvitationVo);

    //invitations list
    List<InvitationVo> list(InvitationVo friendInvitationVo);

}
