package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleMenuRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SysRoleMenuRepositoryImpl implements SysRoleMenuRepository {
    private final SysRoleMenuMapper roleMenuMapper;


    @Override
    public List<SysRoleMenu> listByRoleId(Long roleId) {
        return roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
            .eq(SysRoleMenu::getRoleId, roleId)
        );
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));
    }

    @Override
    public void save(SysRoleMenu sysRoleMenu) {
        roleMenuMapper.insert(sysRoleMenu);
    }

    @Override
    public List<SysRoleMenu> listByRoleIds(List<Long> roleIds) {
        return roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
            .in(SysRoleMenu::getRoleId, roleIds));
    }

    @Override
    public void deleteByMenuIds(List<Long> menuIds) {
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery()
            .in(SysRoleMenu::getMenuId, menuIds));
    }
}
