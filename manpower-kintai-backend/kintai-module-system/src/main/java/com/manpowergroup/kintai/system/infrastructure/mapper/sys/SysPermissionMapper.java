package com.manpowergroup.kintai.system.infrastructure.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import org.apache.ibatis.annotations.Mapper;

// 権限マスタMapper
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
}


