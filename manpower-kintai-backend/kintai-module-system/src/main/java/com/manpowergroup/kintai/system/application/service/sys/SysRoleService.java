package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

import java.util.List;

// ロールマスタサービス（アプリケーション層）
public interface SysRoleService extends IService<SysRole> {

    // IDでロールを取得（存在しない場合は例外）
    SysRole getById(Long id);

    // 会社IDでロール一覧をページング取得
    PageResult<SysRole> page(Long companyId, PageRequest request);

    // 会社IDでロール一覧を全取得
    List<SysRole> listByCompany(Long companyId);

    // ロールを新規作成
    SysRole create(SysRole role);

    // ロールを更新
    SysRole update(Long id, SysRole role);

    // ロールにメニューを割り当て（既存は全削除して再登録）
    void assignMenus(Long roleId, List<Long> menuIds);

    // ロールに権限を割り当て（既存は全削除して再登録）
    void assignPermissions(Long roleId, List<Long> permissionIds);

    // ロールを有効化
    void enable(Long id);

    // ロールを無効化
    void disable(Long id);

    // ロールを削除（論理削除）
    void remove(Long id);
}

