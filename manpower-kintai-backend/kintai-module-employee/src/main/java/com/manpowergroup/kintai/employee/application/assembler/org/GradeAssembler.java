package com.manpowergroup.kintai.employee.application.assembler.org;

import com.manpowergroup.kintai.employee.application.command.org.GradeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.GradeUpdateCommand;
import com.manpowergroup.kintai.employee.application.dto.org.response.GradeResponse;
import com.manpowergroup.kintai.employee.application.dto.org.request.GradeCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.org.request.GradeUpdateRequest;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;

public final class GradeAssembler {

    private GradeAssembler() {
    }

    /**
     * 入力データを職級作成コマンドへ変換する。
     *
     * @param request 変換対象の職級作成リクエスト
     * @return 変換後の職級作成コマンド
     */
    public static GradeCreateCommand toCommand(GradeCreateRequest request) {
        return new GradeCreateCommand(
                request.getCompanyId(), request.getName(), request.getCode(), request.getGradeLevel(),
                request.getSort(), request.getStatus()
        );
    }

    /**
     * 入力データを職級更新コマンドへ変換する。
     *
     * @param request 変換対象の職級更新リクエスト
     * @return 変換後の職級更新コマンド
     */
    public static GradeUpdateCommand toCommand(GradeUpdateRequest request) {
        return new GradeUpdateCommand(
                request.getCompanyId(), request.getName(), request.getCode(), request.getGradeLevel(),
                request.getSort(), request.getStatus()
        );
    }

    /**
     * 入力データを職級レスポンスへ変換する。
     *
     * @param grade 処理対象の職級
     * @return 変換後の職級レスポンス
     */
    public static GradeResponse toResponse(OrgGrade grade) {
        return GradeResponse.from(grade);
    }
}
