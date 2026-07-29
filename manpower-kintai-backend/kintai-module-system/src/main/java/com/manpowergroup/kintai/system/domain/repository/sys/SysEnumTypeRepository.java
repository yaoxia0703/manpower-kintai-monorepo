package com.manpowergroup.kintai.system.domain.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;

import java.util.List;

public interface SysEnumTypeRepository {
    /**
     * IDに対応する列挙型を取得する。
     *
     * @param id 対象の列挙型ID
     * @return 取得した列挙型
     */
    SysEnumType findById(Long id);

    /**
     * コードに対応する列挙型を取得する。
     *
     * @param code 対象コード
     * @return 取得した列挙型
     */
    SysEnumType findByCode(String code);

    /**
     * 指定された条件に一致する有効な列挙型を一覧取得する。
     *
     * @return 列挙型一覧。該当しない場合は空リスト
     */
    List<SysEnumType> listEnabled();

    /**
     * 指定された検索条件で列挙型をページング取得する。
     *
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた列挙型一覧
     */
    PageResult<SysEnumType> page(int page, int size);

    /**
     * 指定コードに一致する列挙型の件数を取得する。
     *
     * @param code 対象コード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean selectCountByCode(String code);

    /**
     * コードを除くに対応する列挙型を取得する。
     *
     * @param id 対象の列挙型ID
     * @param code 対象コード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean findByCodeExcluding(Long id,String code);

    /**
     * 列挙型を保存する。
     *
     * @param sysEnumType 保存対象の列挙型
     */
    void save(SysEnumType sysEnumType);

    /**
     * 列挙型を更新する。
     *
     * @param sysEnumType 保存対象の列挙型
     */
    void updateById(SysEnumType sysEnumType);

    /**
     * 指定IDの列挙型を削除する。
     *
     * @param id 対象の列挙型ID
     */
    void deleteById(Long id);

}
