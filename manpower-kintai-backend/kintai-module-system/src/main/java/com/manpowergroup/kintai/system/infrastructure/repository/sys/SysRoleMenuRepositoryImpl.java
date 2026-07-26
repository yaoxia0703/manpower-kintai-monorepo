package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleMenuRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleMenuRepositoryImpl implements SysRoleMenuRepository {
    private final SysRoleMenuMapper sysRoleMenuMapper;

    public SysRoleMenuRepositoryImpl(SysRoleMenuMapper sysRoleMenuMapper) {
        this.sysRoleMenuMapper = sysRoleMenuMapper;
    }

    @Override
    public List<SysRoleMenu> findByRoleId(Long roleId) {
        return sysRoleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
            .eq(SysRoleMenu::getRoleId, roleId)
        );
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId,roleId));
    }

    @Override
    public void save(SysRoleMenu sysRoleMenu) {
        sysRoleMenuMapper.insert(sysRoleMenu);
    }
}
