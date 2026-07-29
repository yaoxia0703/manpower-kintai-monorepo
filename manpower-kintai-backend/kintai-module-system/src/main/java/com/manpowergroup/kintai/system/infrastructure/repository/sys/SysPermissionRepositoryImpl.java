package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.repository.sys.SysPermissionRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Repository
public class SysPermissionRepositoryImpl implements SysPermissionRepository {
    private final SysPermissionMapper permissionMapper;

    public SysPermissionRepositoryImpl(SysPermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }


    @Override
    public List<SysPermission> listLoadEnabled() {
        return permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
            .eq(SysPermission::getStatus, Status.ENABLED)
            .orderByAsc(SysPermission::getSort));
    }

    @Override
    public List<SysPermission> listAll() {
        return permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery().orderByAsc(SysPermission::getSort));
    }

    @Override
    public SysPermission findById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public PageResult<SysPermission> findPageByMenuIdsAndKeyword(
        List<Long> menuIds, String keyword, int page, int size) {

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();

        if (menuIds != null && !menuIds.isEmpty()) {
            wrapper.in(SysPermission::getMenuId, menuIds);
        }

        String normalized = StringUtils.hasText(keyword) ? keyword.trim() : null;
        if (normalized != null) {
            String codePattern = "%" + escapeLikeLiteral(normalized.toLowerCase(Locale.ROOT)) + "%";
            String namePattern = "%" + escapeLikeLiteral(normalized) + "%";
            wrapper.and(q -> q
                .apply("LOWER(code) LIKE {0} ESCAPE '!'", codePattern)
                .or()
                .apply("name LIKE {0} ESCAPE '!'", namePattern));
        }

        wrapper.orderByAsc(SysPermission::getSort).orderByAsc(SysPermission::getId);

        Page<SysPermission> p = new Page<>(page, size);
        permissionMapper.selectPage(p, wrapper);
        return PageResult.of(p);
    }

    @Override
    public List<SysPermission> listByMenuOrderBySort(Long menuId) {
        return permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
            .eq(SysPermission::getMenuId, menuId)
            .orderByAsc(SysPermission::getSort));
    }

    @Override
    public List<SysPermission> listByIdsOrderBySort(List<Long> ids) {
        return permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
            .in(SysPermission::getId, ids)
            .orderByAsc(SysPermission::getSort));
    }

    @Override
    public void save(SysPermission sysPermission) {
        permissionMapper.insert(sysPermission);
    }

    @Override
    public void updateById(SysPermission sysPermission) {
        permissionMapper.updateById(sysPermission);
    }

    @Override
    public void deleteById(Long id) {
        permissionMapper.deleteById(id);
    }

    @Override
    public boolean existsByCodeExcludingId(String code, Long id) {
        return permissionMapper.selectCount(Wrappers.<SysPermission>lambdaQuery()
            .eq(SysPermission::getCode, code)
            .ne(id != null, SysPermission::getId, id)
        ) > 0;
    }

    @Override
    public boolean existsByMenuIds(List<Long> menuIds) {
        return permissionMapper.selectCount(Wrappers.<SysPermission>lambdaQuery()
            .in(SysPermission::getMenuId, menuIds
            )) > 0;
    }

    @Override
    public List<SysPermission> listEnadbledByIds(List<Long> ids) {
        return permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
            .in(SysPermission::getId, ids)
            .eq(SysPermission::getStatus, Status.ENABLED)
        );
    }


    private String escapeLikeLiteral(String value) {
        return value
            .replace("!", "!!")
            .replace("%", "!%")
            .replace("_", "!_");
    }


}
