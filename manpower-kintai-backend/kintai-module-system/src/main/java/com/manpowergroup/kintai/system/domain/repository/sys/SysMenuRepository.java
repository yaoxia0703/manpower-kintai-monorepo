package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;

import java.util.List;

public interface SysMenuRepository {

    /**
     * すべてのメニューを表示順の昇順で取得する。
     *
     * @return メニュー一覧。該当しない場合は空リスト
     */
    List<SysMenu> listAllOrderBySort();

    /**
     * IDに一致するメニューが存在するか判定する。
     *
     * @param id 対象のメニューID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsById(Long id);

    /**
     * 指定メニュー自身と子孫メニューのIDを一覧取得する。
     *
     * @param rootId 起点となる組織ノードID
     * @return 指定メニュー自身と子孫メニューのID一覧。該当しない場合は空リスト
     */
    List<Long> listSelfAndDescendantIds(Long rootId);

    /**
     * IDに対応するメニューを取得する。
     *
     * @param id 対象のメニューID
     * @return 取得したメニュー
     */
    SysMenu getById(Long id);

    /**
     * 指定IDのメニューを表示順の昇順で取得する。
     *
     * @param ids 対象ID一覧
     * @return メニュー一覧。該当しない場合は空リスト
     */
    List<SysMenu> listByIdsOrderBySort(List<Long> ids);

    /**
     * コードに一致するメニューが存在するか判定する。
     *
     * @param code 対象コード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCode(String code);

    /**
     * メニューを保存する。
     *
     * @param sysMenu 保存対象のメニュー
     */
    void save(SysMenu sysMenu);

    /**
     * メニューを更新する。
     *
     * @param sysMenu 保存対象のメニュー
     */
    void updateById(SysMenu sysMenu);

    /**
     * 指定IDのメニューを削除する。
     *
     * @param id 対象のメニューID
     */
    void deleteById(Long id);

    /**
     * 指定IDを除き、同一条件のメニューが存在するか判定する。
     *
     * @param id 対象のメニューID
     * @param code 対象コード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCodeExcludingId(Long id, String code);

}
