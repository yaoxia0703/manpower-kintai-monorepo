package com.manpowergroup.kintai.employee.application.assembler.emp;

import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionUpdateCommand;
import com.manpowergroup.kintai.employee.application.dto.emp.response.EmployeePositionResponse;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeePositionCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeePositionUpdateRequest;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;

public final class EmployeePositionAssembler {

    private EmployeePositionAssembler() {
    }

    /**
     * 入力データを社員職位作成コマンドへ変換する。
     *
     * @param request 変換対象の社員職位作成リクエスト
     * @return 変換後の社員職位作成コマンド
     */
    public static EmployeePositionCreateCommand toCommand(EmployeePositionCreateRequest request) {
        return new EmployeePositionCreateCommand(
                request.getEmployeeId(), request.getCompanyId(), request.getNodeId(), request.getGradeId(),
                request.getIsPrimary(), request.getStartDate(), request.getEndDate(), request.getStatus()
        );
    }

    /**
     * 入力データを社員職位更新コマンドへ変換する。
     *
     * @param request 変換対象の社員職位更新リクエスト
     * @return 変換後の社員職位更新コマンド
     */
    public static EmployeePositionUpdateCommand toCommand(EmployeePositionUpdateRequest request) {
        return new EmployeePositionUpdateCommand(
                request.getEmployeeId(), request.getCompanyId(), request.getNodeId(), request.getGradeId(),
                request.getIsPrimary(), request.getStartDate(), request.getEndDate(), request.getStatus()
        );
    }

    /**
     * 入力データを社員職位レスポンスへ変換する。
     *
     * @param position 処理対象の社員職位
     * @return 変換後の社員職位レスポンス
     */
    public static EmployeePositionResponse toResponse(EmpEmployeePosition position) {
        return EmployeePositionResponse.from(position);
    }
}
