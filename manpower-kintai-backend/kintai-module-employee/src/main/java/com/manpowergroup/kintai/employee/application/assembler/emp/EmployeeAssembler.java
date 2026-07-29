package com.manpowergroup.kintai.employee.application.assembler.emp;

import com.manpowergroup.kintai.employee.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeUpdateCommand;
import com.manpowergroup.kintai.employee.application.dto.emp.response.EmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeeCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeeUpdateRequest;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;

public final class EmployeeAssembler {

    private EmployeeAssembler() {
    }

    /**
     * 入力データを社員作成コマンドへ変換する。
     *
     * @param request 変換対象の社員作成リクエスト
     * @return 変換後の社員作成コマンド
     */
    public static EmployeeCreateCommand toCommand(EmployeeCreateRequest request) {
        return new EmployeeCreateCommand(
                request.getCompanyId(), request.getEmployeeCode(), request.getLastName(), request.getFirstName(),
                request.getLastNameKana(), request.getFirstNameKana(), request.getEmail(), request.getPhone(),
                request.getGender(), request.getBirthDate(), request.getHireDate(), request.getLeaveDate(),
                request.getStatus()
        );
    }

    /**
     * 入力データを社員更新コマンドへ変換する。
     *
     * @param request 変換対象の社員更新リクエスト
     * @return 変換後の社員更新コマンド
     */
    public static EmployeeUpdateCommand toCommand(EmployeeUpdateRequest request) {
        return new EmployeeUpdateCommand(
                request.getCompanyId(), request.getEmployeeCode(), request.getLastName(), request.getFirstName(),
                request.getLastNameKana(), request.getFirstNameKana(), request.getEmail(), request.getPhone(),
                request.getGender(), request.getBirthDate(), request.getHireDate(), request.getLeaveDate(),
                request.getStatus()
        );
    }

    /**
     * 入力データを社員レスポンスへ変換する。
     *
     * @param employee 処理対象の社員
     * @return 変換後の社員レスポンス
     */
    public static EmployeeResponse toResponse(EmpEmployee employee) {
        return EmployeeResponse.from(employee);
    }
}
