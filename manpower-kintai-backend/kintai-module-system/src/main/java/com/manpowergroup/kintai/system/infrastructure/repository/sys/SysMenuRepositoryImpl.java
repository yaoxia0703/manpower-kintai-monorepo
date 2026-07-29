package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.repository.sys.SysMenuRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class SysMenuRepositoryImpl implements SysMenuRepository {

    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> listAllOrderBySort() {
        return menuMapper.selectList(Wrappers.<SysMenu>lambdaQuery().orderByAsc(SysMenu::getSort));
    }

    // SysMenuRepositoryImpl に追加
    @Override
    public boolean existsById(Long id) {
        return id != null && menuMapper.selectById(id) != null;
    }

    @Override
    public List<Long> listSelfAndDescendantIds(Long rootId) {
        List<SysMenu> menus = menuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
            .select(SysMenu::getId, SysMenu::getParentId));

        if (menus.stream().noneMatch(menu -> rootId.equals(menu.getId()))) {
            return List.of();
        }

        Set<Long> collected = new LinkedHashSet<>();
        collected.add(rootId);
        boolean changed;
        do {
            changed = false;
            for (SysMenu menu : menus) {
                if (menu.getId() != null
                    && menu.getParentId() != null
                    && collected.contains(menu.getParentId())
                    && collected.add(menu.getId())) {
                    changed = true;
                }
            }
        } while (changed);
        return List.copyOf(collected);
    }

    @Override
    public SysMenu getById(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    public List<SysMenu> listByIdsOrderBySort(List<Long> ids) {
        return menuMapper.selectList(Wrappers.<SysMenu>lambdaQuery().in(SysMenu::getId, ids).orderByAsc(SysMenu::getSort));
    }

    @Override
    public boolean existsByCode(String code) {
        return menuMapper.selectCount(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getCode, code)) > 0;
    }

    @Override
    public void save(SysMenu sysMenu) {
        menuMapper.insert(sysMenu);
    }

    @Override
    public void updateById(SysMenu sysMenu) {
        menuMapper.updateById(sysMenu);
    }

    @Override
    public void deleteById(Long id) {
        menuMapper.deleteById(id);
    }

    @Override
    public boolean existsByCodeExcludingId(Long id, String code) {
        return menuMapper.selectCount(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getCode, code)
            .ne(SysMenu::getId, id)) > 0;
    }
}
