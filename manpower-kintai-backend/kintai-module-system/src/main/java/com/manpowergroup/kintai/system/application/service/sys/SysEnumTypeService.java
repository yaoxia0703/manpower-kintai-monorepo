package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.EnumTypeCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumTypeUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;

import java.util.List;

// 列挙型マスタサービス（アプリケーション層）
public interface SysEnumTypeService{

    /**
     * IDに対応する列挙型を取得する。
     *
     * @param id 対象の列挙型ID
     * @return 取得した列挙型
     */
    SysEnumType getById(Long id);

    /**
     * コードに対応する列挙型を取得する。
     *
     * @param code 対象コード
     * @return 取得した列挙型
     */
    SysEnumType getByCode(String code);

    /**
     * 指定された条件に一致する有効な列挙型を一覧取得する。
     *
     * @return 列挙型一覧。該当しない場合は空リスト
     */
    List<SysEnumType> listEnabled();

    /**
     * 指定された検索条件で列挙型をページング取得する。
     *
     * @param request 処理対象のページング条件
     * @return ページングされた列挙型一覧
     */
    PageResult<SysEnumType> page(PageRequest request);

    /**
     * 列挙型を新規作成する。
     *
     * @param command 処理対象の列挙型作成コマンド
     * @return 保存した列挙型
     */
    SysEnumType create(EnumTypeCreateCommand command);

    /**
     * 列挙型を更新する。
     *
     * @param id 対象の列挙型ID
     * @param command 処理対象の列挙型更新コマンド
     * @return 更新した列挙型
     */
    SysEnumType update(Long id, EnumTypeUpdateCommand command);

    /**
     * 列挙型を有効化する。
     *
     * @param id 対象の列挙型ID
     */
    void enable(Long id);

    /**
     * 列挙型を無効化する。
     *
     * @param id 対象の列挙型ID
     */
    void disable(Long id);

    /**
     * 指定IDの列挙型を削除する。
     *
     * @param id 対象の列挙型ID
     */
    void remove(Long id);
}

