package com.manpowergroup.kintai.system.infrastructure.mapper.emp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
import org.apache.ibatis.annotations.Mapper;

// 社員アカウントMapper
@Mapper
public interface EmpAccountMapper extends BaseMapper<EmpAccount> {
}


