package com.manpowergroup.kintai.system.infrastructure.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import org.apache.ibatis.annotations.Mapper;

// メニューマスタMapper
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
}


