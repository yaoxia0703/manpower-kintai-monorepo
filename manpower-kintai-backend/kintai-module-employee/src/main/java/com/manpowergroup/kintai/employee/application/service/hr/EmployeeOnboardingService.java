package com.manpowergroup.kintai.employee.application.service.hr;

import com.manpowergroup.kintai.employee.application.dto.hr.request.EmployeeOnboardingRequest;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingOptionsResponse;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingResponse;

public interface EmployeeOnboardingService {

    /**
     * 画面表示に必要な選択肢を取得する。
     *
     * @param operatorEmployeeId 操作を行う社員ID
     * @param companyId 対象の会社ID
     * @return 入社登録選択肢レスポンス
     */
    EmployeeOnboardingOptionsResponse options(Long operatorEmployeeId, Long companyId);

    /**
     * 社員の入社登録を実行する。
     *
     * @param request 処理対象の入社登録リクエスト
     * @param operatorEmployeeId 操作を行う社員ID
     * @return 入社登録レスポンス
     */
    EmployeeOnboardingResponse onboard(EmployeeOnboardingRequest request, Long operatorEmployeeId);
}
