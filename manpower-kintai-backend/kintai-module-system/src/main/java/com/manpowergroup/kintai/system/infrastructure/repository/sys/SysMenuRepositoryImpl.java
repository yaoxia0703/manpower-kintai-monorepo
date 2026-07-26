package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.repository.sys.SysMenuRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysMenuRepositoryImpl implements SysMenuRepository {

    private final SysMenuMapper sysMenuMapper;

    public SysMenuRepositoryImpl(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }


    @Override
    public List<SysMenu> findAll() {
        return sysMenuMapper.selectList(Wrappers.<SysMenu>lambdaQuery().orderByAsc(SysMenu::getSort));
    }
}
