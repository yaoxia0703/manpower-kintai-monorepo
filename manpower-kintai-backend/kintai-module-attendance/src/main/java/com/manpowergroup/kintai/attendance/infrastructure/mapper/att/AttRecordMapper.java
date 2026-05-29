package com.manpowergroup.kintai.attendance.infrastructure.mapper.att;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import org.apache.ibatis.annotations.Mapper;

// 打刻記録Mapper
@Mapper
public interface AttRecordMapper extends BaseMapper<AttRecord> {
}
