package com.manpowergroup.kintai.employee.infrastructure.mapper.emp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;
import org.apache.ibatis.annotations.Mapper;

// 社員職位関連Mapper
@Mapper
public interface EmpEmployeePositionMapper extends BaseMapper<EmpEmployeePosition> {
}


