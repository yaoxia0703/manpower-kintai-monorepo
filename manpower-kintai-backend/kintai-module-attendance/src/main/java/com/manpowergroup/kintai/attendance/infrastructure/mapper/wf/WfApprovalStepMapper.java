package com.manpowergroup.kintai.attendance.infrastructure.mapper.wf;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalStep;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WfApprovalStepMapper extends BaseMapper<WfApprovalStep> {
}
