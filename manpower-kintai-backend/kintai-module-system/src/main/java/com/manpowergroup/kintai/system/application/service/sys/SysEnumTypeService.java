package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.EnumTypeCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumTypeUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;

import java.util.List;

// 列挙型マスタサービス（アプリケーション層）
public interface SysEnumTypeService extends IService<SysEnumType> {

    // IDで列挙型を取得（存在しない場合は例外）
    SysEnumType getById(Long id);

    // コードで列挙型を取得
    SysEnumType getByCode(String code);

    // 有効な全列挙型を取得
    List<SysEnumType> listEnabled();

    // 列挙型一覧をページング取得
    PageResult<SysEnumType> page(PageRequest request);

    // 列挙型を新規作成
    SysEnumType create(EnumTypeCreateCommand command);

    // 列挙型を更新
    SysEnumType update(Long id, EnumTypeUpdateCommand command);

    // 列挙型を有効化
    void enable(Long id);

    // 列挙型を無効化
    void disable(Long id);

    // 列挙型を削除（論理削除）
    void remove(Long id);
}

