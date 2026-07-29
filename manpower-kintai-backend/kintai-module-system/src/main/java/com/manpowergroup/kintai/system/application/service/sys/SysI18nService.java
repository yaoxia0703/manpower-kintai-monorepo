package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.sys.I18nUpsertCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;

import java.util.List;

// 国際化翻訳サービス（アプリケーション層）
public interface SysI18nService  {

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
     * 多言語情報を登録または更新する。
     *
     * @param command 処理対象の多言語情報登録・更新コマンド
     * @return 多言語情報
     */
    SysI18n upsert(I18nUpsertCommand command);

    /**
     * 指定IDの多言語情報を削除する。
     *
     * @param id 対象の多言語情報ID
     */
    void remove(Long id);

    /**
     * 参照情報に対応する多言語情報を削除する。
     *
     * @param refType 関連データの種別
     * @param refId 関連データのID
     */
    void removeByRef(String refType, Long refId);
}

