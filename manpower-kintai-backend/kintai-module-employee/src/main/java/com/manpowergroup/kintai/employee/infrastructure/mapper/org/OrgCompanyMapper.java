package com.manpowergroup.kintai.employee.infrastructure.mapper.org;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;
import org.apache.ibatis.annotations.Mapper;

// 会社マスタMapper
@Mapper
public interface OrgCompanyMapper extends BaseMapper<OrgCompany> {
}


