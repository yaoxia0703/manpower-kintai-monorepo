package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;

import java.util.List;

public interface SysMenuRepository {

    List<SysMenu> listAllOrderBySort();

    // メニューの存在確認
    boolean existsById(Long id);

    // ルートメニュー自身とその全子孫IDを取得（ルートが存在しない場合は空リスト）
    List<Long> listSelfAndDescendantIds(Long rootId);

    SysMenu getById(Long id);

    List<SysMenu> listByIdsOrderBySort(List<Long> ids);

    boolean existsByCode(String code);

    void save(SysMenu sysMenu);

    void updateById(SysMenu sysMenu);

    void deleteById(Long id);

    boolean existsByCodeExcludingId(Long id, String code);

}
