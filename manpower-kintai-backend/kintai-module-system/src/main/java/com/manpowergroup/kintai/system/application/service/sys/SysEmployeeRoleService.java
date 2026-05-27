package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

import java.util.List;

// 社員ロール関連サービス（アプリケーション層）
public interface SysEmployeeRoleService extends IService<SysEmployeeRole> {

    // IDで社員ロールを取得（存在しない場合は例外）
    SysEmployeeRole getById(Long id);

    // 社員IDで有効なロール一覧を取得
    List<SysEmployeeRole> listActiveByEmployee(Long employeeId);

    // 社員ロールを追加（重複チェックあり）
    SysEmployeeRole assign(SysEmployeeRole employeeRole);

    // 社員ロールを更新（有効期間の変更等）
    SysEmployeeRole update(Long id, SysEmployeeRole employeeRole);

    // 社員からロールを剥奪（論理削除）
    void revoke(Long id);
}

