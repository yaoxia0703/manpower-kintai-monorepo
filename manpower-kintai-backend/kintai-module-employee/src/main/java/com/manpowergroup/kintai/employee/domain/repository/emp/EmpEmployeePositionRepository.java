package com.manpowergroup.kintai.employee.domain.repository.emp;

import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;

import java.util.List;

public interface EmpEmployeePositionRepository {

    /**
     * IDに対応する社員職位を取得する。
     *
     * @param id 対象の社員職位ID
     * @return 取得した社員職位
     */
    EmpEmployeePosition getById(long id);

    /**
     * 指定社員・対象期間に対応する社員職位を取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員職位一覧。該当しない場合は空リスト
     */
    List<EmpEmployeePosition> getByEmployeeIdAndStartDateAndEndDate(long employeeId);

    /**
     * 社員IDに対応する社員職位を取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員職位一覧。該当しない場合は空リスト
     */
    List<EmpEmployeePosition> getByEmployeeId(long employeeId);

    /**
     * 指定社員の主職位を取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 取得した社員職位
     */
    EmpEmployeePosition getPrimaryByEmployee(long employeeId);

    /**
     * 社員職位を保存する。
     *
     * @param empEmployeePosition 保存対象の社員職位
     */
    void save(EmpEmployeePosition empEmployeePosition);

    /**
     * 社員職位を更新する。
     *
     * @param empEmployeePosition 保存対象の社員職位
     */
    void updateById(EmpEmployeePosition empEmployeePosition);
    /**
     * 指定IDの社員職位を削除する。
     *
     * @param id 対象の社員職位ID
     */
    void deleteById(long id);
}
