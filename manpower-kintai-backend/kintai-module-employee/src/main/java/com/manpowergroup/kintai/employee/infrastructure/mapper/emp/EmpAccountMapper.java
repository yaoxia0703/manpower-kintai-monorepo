package com.manpowergroup.kintai.employee.infrastructure.mapper.emp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import org.apache.ibatis.annotations.Mapper;

// 社員アカウントMapper
@Mapper
public interface EmpAccountMapper extends BaseMapper<EmpAccount> {
}


