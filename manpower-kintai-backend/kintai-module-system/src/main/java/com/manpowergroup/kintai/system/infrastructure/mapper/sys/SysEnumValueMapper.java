package com.manpowergroup.kintai.system.infrastructure.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;
import org.apache.ibatis.annotations.Mapper;

// 列挙値定義Mapper
@Mapper
public interface SysEnumValueMapper extends BaseMapper<SysEnumValue> {
}


