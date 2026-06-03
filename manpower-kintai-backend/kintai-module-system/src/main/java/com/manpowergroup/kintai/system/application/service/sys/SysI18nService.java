package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.sys.I18nUpsertCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;

import java.util.List;

// 国際化翻訳サービス（アプリケーション層）
public interface SysI18nService extends IService<SysI18n> {

    // IDで翻訳を取得（存在しない場合は例外）
    SysI18n getById(Long id);

    // 参照タイプとレコードIDで翻訳一覧を取得
    List<SysI18n> listByRef(String refType, Long refId);

    // 参照タイプ・レコードID・言語で翻訳を取得
    SysI18n getByRefAndLanguage(String refType, Long refId, String language);

    // 翻訳を保存（新規作成または上書き更新）
    // IService#saveOrUpdate は boolean を返すため、エンティティを返す本メソッドは upsert に改名
    SysI18n upsert(I18nUpsertCommand command);

    // 翻訳を削除（論理削除）
    void remove(Long id);

    // 参照レコードに紐づく全翻訳を削除
    void removeByRef(String refType, Long refId);
}

