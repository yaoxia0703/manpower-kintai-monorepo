package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;

import java.util.List;

// 権限マスタサービス（アプリケーション層）
public interface SysPermissionService extends IService<SysPermission> {

    // IDで権限を取得（存在しない場合は例外）
    SysPermission getById(Long id);

    // 権限一覧をページング取得
    PageResult<SysPermission> page(PageRequest request);

    // メニューIDで権限一覧を取得
    List<SysPermission> listByMenu(Long menuId);

    // 社員IDに基づく権限一覧を取得（ロール経由）
    List<SysPermission> listByEmployeeId(Long employeeId);

    // 権限を新規作成
    SysPermission create(SysPermission permission);

    // 権限を更新
    SysPermission update(Long id, SysPermission permission);

    // 権限を有効化
    void enable(Long id);

    // 権限を無効化
    void disable(Long id);

    // 権限を削除（論理削除）
    void remove(Long id);
}

