package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;

import java.util.List;

// 列挙値定義サービス（アプリケーション層）
public interface SysEnumValueService  {

    /**
     * IDに対応する列挙値を取得する。
     *
     * @param id 対象の列挙値ID
     * @return 取得した列挙値
     */
    SysEnumValue getById(Long id);

    /**
     * 列挙型コードに一致する列挙値を一覧取得する。
     *
     * @param enumTypeCode 対象の列挙型コード
     * @return 列挙値一覧。該当しない場合は空リスト
     */
    List<SysEnumValue> listByEnumTypeCode(String enumTypeCode);

    /**
     * 列挙値を新規作成する。
     *
     * @param command 処理対象の列挙値作成コマンド
     * @return 保存した列挙値
     */
    SysEnumValue create(EnumValueCreateCommand command);

    /**
     * 列挙値を更新する。
     *
     * @param id 対象の列挙値ID
     * @param command 処理対象の列挙値更新コマンド
     * @return 更新した列挙値
     */
    SysEnumValue update(Long id, EnumValueUpdateCommand command);

    /**
     * 列挙値を有効化する。
     *
     * @param id 対象の列挙値ID
     */
    void enable(Long id);

    /**
     * 列挙値を無効化する。
     *
     * @param id 対象の列挙値ID
     */
    void disable(Long id);

    /**
     * 指定IDの列挙値を削除する。
     *
     * @param id 対象の列挙値ID
     */
    void remove(Long id);
}

