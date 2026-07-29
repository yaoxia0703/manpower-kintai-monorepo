package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;

import java.util.List;

public interface SysI18nRepository {

    /**
     * IDに対応する多言語情報を取得する。
     *
     * @param id 対象の多言語情報ID
     * @return 取得した多言語情報
     */
    SysI18n getById(Long id);

    /**
     * 参照情報に一致する多言語情報を一覧取得する。
     *
     * @param refType 関連データの種別
     * @param refId 関連データのID
     * @return 多言語情報一覧。該当しない場合は空リスト
     */
    List<SysI18n> listByRef(String refType, Long refId);

    /**
     * 参照情報および言語に対応する多言語情報を取得する。
     *
     * @param refType 関連データの種別
     * @param refId 関連データのID
     * @param language 対象の言語コード
     * @return 取得した多言語情報
     */
    SysI18n getByRefAndLanguage(String refType, Long refId, String language);

    /**
     * 多言語情報を保存する。
     *
     * @param sysI18n 保存対象の多言語情報
     */
    void save(SysI18n sysI18n);

    /**
     * 多言語情報を更新する。
     *
     * @param sysI18n 保存対象の多言語情報
     */
    void updateById(SysI18n sysI18n);

    /**
     * 指定IDの多言語情報を削除する。
     *
     * @param id 対象の多言語情報ID
     */
    void deleteById(Long id);

    /**
     * 参照情報に対応する多言語情報を削除する。
     *
     * @param refType 関連データの種別
     * @param refId 関連データのID
     */
    void deleteByRef(String refType, Long refId);
}
