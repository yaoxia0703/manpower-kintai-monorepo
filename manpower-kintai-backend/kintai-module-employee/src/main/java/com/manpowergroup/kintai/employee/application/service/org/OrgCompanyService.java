package com.manpowergroup.kintai.employee.application.service.org;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.command.org.CompanyCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.CompanyUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;

import java.util.List;

// 会社マスタサービス（アプリケーション層）
public interface OrgCompanyService {

    /**
     * IDに対応する会社を取得する。
     *
     * @param id 対象の会社ID
     * @return 取得した会社
     */
    OrgCompany getById(Long id);

    /**
     * 指定された検索条件で会社をページング取得する。
     *
     * @param request 処理対象のページング条件
     * @return ページングされた会社一覧
     */
    PageResult<OrgCompany> page(PageRequest request);

    /**
     * 指定された条件に一致する有効な会社を一覧取得する。
     *
     * @return 会社一覧。該当しない場合は空リスト
     */
    List<OrgCompany> listEnabled();

    /**
     * 会社を新規作成する。
     *
     * @param command 処理対象の会社作成コマンド
     * @return 保存した会社
     */
    OrgCompany create(CompanyCreateCommand command);

    /**
     * 会社を更新する。
     *
     * @param id 対象の会社ID
     * @param command 処理対象の会社更新コマンド
     * @return 更新した会社
     */
    OrgCompany update(Long id, CompanyUpdateCommand command);

    /**
     * 会社を有効化する。
     *
     * @param id 対象の会社ID
     */
    void enable(Long id);

    /**
     * 会社を無効化する。
     *
     * @param id 対象の会社ID
     */
    void disable(Long id);

    /**
     * 指定IDの会社を削除する。
     *
     * @param id 対象の会社ID
     */
    void remove(Long id);

    boolean companyExists(Long id);
}

