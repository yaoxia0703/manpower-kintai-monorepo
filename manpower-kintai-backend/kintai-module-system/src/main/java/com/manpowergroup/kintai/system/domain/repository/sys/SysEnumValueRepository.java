package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;

import java.util.List;

public interface SysEnumValueRepository {

    /**
     * IDに対応する列挙値を取得する。
     *
     * @param id 対象の列挙値ID
     * @return 取得した列挙値
     */
    SysEnumValue findById(Long id);

    /**
     * 指定された条件に一致する有効な列挙値を一覧取得する。
     *
     * @param enumTypeCode 対象の列挙型コード
     * @return 列挙値一覧。該当しない場合は空リスト
     */
    List<SysEnumValue> listEnabledByEnumTypeCode(String enumTypeCode);

    /**
     * 指定IDを除き、同一条件の列挙値が存在するか判定する。
     *
     * @param enumTypeCode 対象の列挙型コード
     * @param code 対象コード
     * @param excludeId 重複確認から除外するID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByEnumTypeCodeAndCodeExcludingId(String enumTypeCode, String code, Long excludeId);

    /**
     * 列挙値を保存する。
     *
     * @param sysEnumValue 保存対象の列挙値
     */
    void save(SysEnumValue sysEnumValue);

    /**
     * 列挙値を更新する。
     *
     * @param sysEnumValue 保存対象の列挙値
     */
    void updateById(SysEnumValue sysEnumValue);

    /**
     * 指定IDの列挙値を削除する。
     *
     * @param id 対象の列挙値ID
     */
    void deleteById(Long id);
}
