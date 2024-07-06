package com.unimatch.unimatch_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unimatch.unimatch_backend.model.entity.Message;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {
    List<Message> list(@Param("message") Message message);
}
