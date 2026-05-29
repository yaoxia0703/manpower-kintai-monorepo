package com.manpowergroup.kintai.attendance.infrastructure.mapper.att;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttMonthlySummary;
import org.apache.ibatis.annotations.Mapper;

// 月次勤怠集計Mapper
@Mapper
public interface AttMonthlySummaryMapper extends BaseMapper<AttMonthlySummary> {
}
