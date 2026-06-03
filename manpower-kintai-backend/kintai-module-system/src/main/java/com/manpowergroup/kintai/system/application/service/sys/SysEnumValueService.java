package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;

import java.util.List;

// 列挙値定義サービス（アプリケーション層）
public interface SysEnumValueService extends IService<SysEnumValue> {

    // IDで列挙値を取得（存在しない場合は例外）
    SysEnumValue getById(Long id);

    // 列挙型コードで列挙値一覧を取得
    List<SysEnumValue> listByEnumTypeCode(String enumTypeCode);

    // 列挙値を新規作成
    SysEnumValue create(EnumValueCreateCommand command);

    // 列挙値を更新
    SysEnumValue update(Long id, EnumValueUpdateCommand command);

    // 列挙値を有効化
    void enable(Long id);

    // 列挙値を無効化
    void disable(Long id);

    // 列挙値を削除（論理削除）
    void remove(Long id);
}

