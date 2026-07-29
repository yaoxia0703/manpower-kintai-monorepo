package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;

import java.util.List;

public interface SysPermissionRepository {

    /**
     * 有効な権限を一覧取得する。
     *
     * @return 権限一覧。該当しない場合は空リスト
     */
    List<SysPermission> listLoadEnabled();

    /**
     * すべての権限を一覧取得する。
     *
     * @return 権限一覧。該当しない場合は空リスト
     */
    List<SysPermission> listAll();

    /**
     * IDに対応する権限を取得する。
     *
     * @param id 対象の権限ID
     * @return 取得した権限
     */
    SysPermission findById(Long id);

    /**
     * 指定メニューとキーワードで権限をページング取得する。
     *
     * @param menuIds 対象のメニューID一覧
     * @param keyword 検索キーワード。未指定の場合は絞り込まない
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた権限一覧
     */
    PageResult<SysPermission> findPageByMenuIdsAndKeyword(
        List<Long> menuIds, String keyword, int page, int size);

    /**
     * 指定メニューの権限を表示順の昇順で取得する。
     *
     * @param menuId 対象のメニューID
     * @return 権限一覧。該当しない場合は空リスト
     */
    List<SysPermission> listByMenuOrderBySort(Long menuId);

    /**
     * 指定IDの権限を表示順の昇順で取得する。
     *
     * @param ids 対象ID一覧
     * @return 権限一覧。該当しない場合は空リスト
     */
    List<SysPermission>listByIdsOrderBySort(List<Long> ids);

    /**
     * 権限を保存する。
     *
     * @param sysPermission 保存対象の権限
     */
    void save(SysPermission sysPermission);

    /**
     * 権限を更新する。
     *
     * @param sysPermission 保存対象の権限
     */
    void updateById(SysPermission sysPermission);

    /**
     * 指定IDの権限を削除する。
     *
     * @param id 対象の権限ID
     */
    void deleteById(Long id);

    /**
     * 指定IDを除き、同一条件の権限が存在するか判定する。
     *
     * @param code 対象コード
     * @param id 対象の権限ID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCodeExcludingId(String code, Long id);

    /**
     * メニューID一覧に一致する権限が存在するか判定する。
     *
     * @param menuIds 対象のメニューID一覧
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByMenuIds(List<Long> menuIds);

    /**
     * 指定IDの有効な権限を一覧取得する。
     *
     * @param ids 対象ID一覧
     * @return 権限一覧。該当しない場合は空リスト
     */
    List<SysPermission> listEnadbledByIds(List<Long> ids);

}
