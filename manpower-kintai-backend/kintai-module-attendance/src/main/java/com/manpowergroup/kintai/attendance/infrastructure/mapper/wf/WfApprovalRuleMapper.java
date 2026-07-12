package com.manpowergroup.kintai.attendance.infrastructure.mapper.wf;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WfApprovalRuleMapper extends BaseMapper<WfApprovalRule> {
}
