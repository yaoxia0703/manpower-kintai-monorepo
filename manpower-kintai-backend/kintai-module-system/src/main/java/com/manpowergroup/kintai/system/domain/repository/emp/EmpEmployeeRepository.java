package com.manpowergroup.kintai.system.domain.repository.emp;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;

import java.util.List;
import java.util.Optional;

// 社員マスタリポジトリ（ドメイン層インターフェース）
public interface EmpEmployeeRepository {

    // IDで社員を取得
    Optional<EmpEmployee> findById(Long id);

    // メールアドレスで社員を取得
    Optional<EmpEmployee> findByEmail(String email);

    // 社員番号で社員を取得（会社内）
    Optional<EmpEmployee> findByEmployeeCode(Long companyId, String employeeCode);

    // 会社IDでページング取得
    PageResult<EmpEmployee> findPageByCompanyId(Long companyId, int page, int size);

    // 氏名キーワードで検索（ページング）
    PageResult<EmpEmployee> searchByName(Long companyId, String keyword, int page, int size);

    // 社員を保存（新規）
    EmpEmployee save(EmpEmployee employee);

    // 社員を更新
    EmpEmployee update(EmpEmployee employee);

    // IDで論理削除（退職処理）
    void removeById(Long id);

    // メールアドレスの重複チェック（自分自身を除く）
    boolean existsByEmail(String email, Long excludeId);
}


