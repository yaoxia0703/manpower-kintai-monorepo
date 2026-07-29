package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;

import java.util.List;

public interface SysPermissionRepository {

    List<SysPermission> listLoadEnabled();

    List<SysPermission> listAll();

    SysPermission findById(Long id);

    PageResult<SysPermission> findPageByMenuIdsAndKeyword(
        List<Long> menuIds, String keyword, int page, int size);

    List<SysPermission> listByMenuOrderBySort(Long menuId);

    List<SysPermission>listByIdsOrderBySort(List<Long> ids);

    void save(SysPermission sysPermission);

    void updateById(SysPermission sysPermission);

    void deleteById(Long id);

    boolean existsByCodeExcludingId(String code, Long id);

    boolean existsByMenuIds(List<Long> menuIds);

    List<SysPermission> listEnadbledByIds(List<Long> ids);

}
