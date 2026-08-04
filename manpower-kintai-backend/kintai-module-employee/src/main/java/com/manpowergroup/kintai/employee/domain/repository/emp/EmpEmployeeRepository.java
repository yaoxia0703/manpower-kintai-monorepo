package com.manpowergroup.kintai.employee.domain.repository.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryFilter;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryResponse;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;

public interface EmpEmployeeRepository {
    /**
     * IDに対応する社員を取得する。
     *
     * @param id 対象の社員ID
     * @return 取得した社員
     */
    EmpEmployee getById(Long id);

    /**
     * メールアドレスに対応する社員を取得する。
     *
     * @param email 対象のメールアドレス
     * @return 取得した社員
     */
    EmpEmployee findByEmail(String email);

    /**
     * 指定会社の社員をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param request 処理対象のページング条件
     * @return ページングされた社員一覧
     */
    PageResult<EmpEmployee> findPageByCompany(long companyId, PageRequest request);

    /**
     * 指定会社とキーワードで社員をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param keyword 検索キーワード。未指定の場合は絞り込まない
     * @param request 処理対象のページング条件
     * @return ページングされた社員一覧
     */
    PageResult<EmpEmployee> findPageByCompanyAndKeyword(long companyId,String keyword,PageRequest request);

    /**
     * 社員を保存する。
     *
     * @param empEmployee 保存対象の社員
     */
    void save(EmpEmployee empEmployee);

    /**
     * 社員を更新する。
     *
     * @param empEmployee 保存対象の社員
     */
    void updateById(EmpEmployee empEmployee);

    /**
     * 指定IDの社員を削除する。
     *
     * @param id 対象の社員ID
     */
    void deleteById(Long id);

    /**
     * メールアドレスに一致する社員が存在するか判定する。
     *
     * @param email 対象のメールアドレス
     * @param excludeId 重複確認から除外するID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByEmail(String email, Long excludeId);

    JoinPageResult<EmployeeDirectoryResponse> pageDirectory(EmployeeDirectoryFilter filter, int page, int size);
}
