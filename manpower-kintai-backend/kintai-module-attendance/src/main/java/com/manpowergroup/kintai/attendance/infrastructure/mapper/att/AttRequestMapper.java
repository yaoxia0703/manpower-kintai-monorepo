package com.manpowergroup.kintai.attendance.infrastructure.mapper.att;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttRequestMapper extends BaseMapper<AttRequest> {
}
