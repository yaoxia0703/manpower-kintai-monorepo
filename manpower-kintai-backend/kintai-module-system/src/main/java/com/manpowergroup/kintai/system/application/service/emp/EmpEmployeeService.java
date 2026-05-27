package com.manpowergroup.kintai.system.application.service.emp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;

// 社員マスタサービス（アプリケーション層）
public interface EmpEmployeeService extends IService<EmpEmployee> {

    // IDで社員を取得（存在しない場合は例外）
    EmpEmployee getById(Long id);

    // 会社IDで社員一覧をページング取得
    PageResult<EmpEmployee> pageByCompany(Long companyId, PageRequest request);

    // 氏名キーワードで社員を検索
    PageResult<EmpEmployee> searchByName(Long companyId, String keyword, PageRequest request);

    // 社員を新規作成
    EmpEmployee create(EmpEmployee employee);

    // 社員情報を更新
    EmpEmployee update(Long id, EmpEmployee employee);

    // 社員を有効化（在職）
    void enable(Long id);

    // 社員を無効化（退職）
    void disable(Long id);

    // 社員を削除（論理削除）
    void remove(Long id);
}

