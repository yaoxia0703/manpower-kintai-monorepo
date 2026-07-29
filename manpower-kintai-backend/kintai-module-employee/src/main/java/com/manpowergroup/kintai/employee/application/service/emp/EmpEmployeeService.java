package com.manpowergroup.kintai.employee.application.service.emp;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;

import java.util.Optional;

public interface EmpEmployeeService {

    /**
     * IDに対応する社員を取得する。
     *
     * @param id 対象の社員ID
     * @return 対象が存在する場合は社員を含むOptional、存在しない場合はOptional.empty()
     */
    Optional<EmpEmployee> findById(Long id);

    /**
     * メールアドレスに対応する社員を取得する。
     *
     * @param email 対象のメールアドレス
     * @return 対象が存在する場合は社員を含むOptional、存在しない場合はOptional.empty()
     */
    Optional<EmpEmployee> findByEmail(String email);

    /**
     * IDに対応する社員を取得する。
     *
     * @param id 対象の社員ID
     * @return 取得した社員
     */
    EmpEmployee getById(Long id);

    /**
     * 指定された検索条件で社員をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param request 処理対象のページング条件
     * @return ページングされた社員一覧
     */
    PageResult<EmpEmployee> pageByCompany(Long companyId, PageRequest request);

    /**
     * 指定された検索条件で社員をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param keyword 検索キーワード。未指定の場合は絞り込まない
     * @param request 処理対象のページング条件
     * @return ページングされた社員一覧
     */
    PageResult<EmpEmployee> searchByName(Long companyId, String keyword, PageRequest request);

    /**
     * 社員を新規作成する。
     *
     * @param command 処理対象の社員作成コマンド
     * @return 保存した社員
     */
    EmpEmployee create(EmployeeCreateCommand command);

    /**
     * 社員を更新する。
     *
     * @param id 対象の社員ID
     * @param command 処理対象の社員更新コマンド
     * @return 更新した社員
     */
    EmpEmployee update(Long id, EmployeeUpdateCommand command);

    /**
     * 社員を有効化する。
     *
     * @param id 対象の社員ID
     */
    void enable(Long id);

    /**
     * 社員を無効化する。
     *
     * @param id 対象の社員ID
     */
    void disable(Long id);

    /**
     * 指定IDの社員を削除する。
     *
     * @param id 対象の社員ID
     */
    void remove(Long id);
}
