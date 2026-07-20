package com.manpowergroup.kintai.employee.application.service.org;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.command.org.CompanyCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.CompanyUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;

import java.util.List;

// 会社マスタサービス（アプリケーション層）
public interface OrgCompanyService extends IService<OrgCompany> {

    // IDで会社を取得（存在しない場合は例外）
    OrgCompany getById(Long id);

    // ページングで会社一覧を取得
    PageResult<OrgCompany> page(PageRequest request);

    // 有効な全会社をツリー用に取得
    List<OrgCompany> listEnabled();

    // 会社を新規作成
    OrgCompany create(CompanyCreateCommand command);

    // 会社を更新
    OrgCompany update(Long id, CompanyUpdateCommand command);

    // 会社を有効化
    void enable(Long id);

    // 会社を無効化
    void disable(Long id);

    // 会社を削除（論理削除）
    void remove(Long id);
}

