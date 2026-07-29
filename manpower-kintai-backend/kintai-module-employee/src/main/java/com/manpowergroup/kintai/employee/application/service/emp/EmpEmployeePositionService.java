package com.manpowergroup.kintai.employee.application.service.emp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;

import java.util.List;

public interface EmpEmployeePositionService  {

    /**
     * IDに対応する社員職位を取得する。
     *
     * @param id 対象の社員職位ID
     * @return 取得した社員職位
     */
    EmpEmployeePosition getById(Long id);

    /**
     * 指定された条件に一致する有効な社員職位を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員職位一覧。該当しない場合は空リスト
     */
    List<EmpEmployeePosition> listActiveByEmployee(Long employeeId);

    /**
     * 社員に一致する社員職位を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員職位一覧。該当しない場合は空リスト
     */
    List<EmpEmployeePosition> listAllByEmployee(Long employeeId);

    /**
     * 指定社員の主職位を取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 取得した社員職位
     */
    EmpEmployeePosition getPrimaryByEmployee(Long employeeId);

    /**
     * 社員職位を新規作成する。
     *
     * @param command 処理対象の社員職位作成コマンド
     * @return 保存した社員職位
     */
    EmpEmployeePosition create(EmployeePositionCreateCommand command);

    /**
     * 社員職位を更新する。
     *
     * @param id 対象の社員職位ID
     * @param command 処理対象の社員職位更新コマンド
     * @return 更新した社員職位
     */
    EmpEmployeePosition update(Long id, EmployeePositionUpdateCommand command);

    /**
     * 社員職位を終了状態に変更する。
     *
     * @param id 対象の社員職位ID
     */
    void terminate(Long id);

    /**
     * 指定IDの社員職位を削除する。
     *
     * @param id 対象の社員職位ID
     */
    void remove(Long id);
}
