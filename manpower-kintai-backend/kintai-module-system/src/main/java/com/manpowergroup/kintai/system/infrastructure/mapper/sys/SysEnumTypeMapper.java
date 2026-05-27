package com.manpowergroup.kintai.system.infrastructure.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;
import org.apache.ibatis.annotations.Mapper;

// 列挙型マスタMapper
@Mapper
public interface SysEnumTypeMapper extends BaseMapper<SysEnumType> {
}


