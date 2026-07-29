package com.manpowergroup.kintai.employee.application.assembler.org;

import com.manpowergroup.kintai.employee.application.command.org.CompanyCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.CompanyUpdateCommand;
import com.manpowergroup.kintai.employee.application.dto.org.response.CompanyResponse;
import com.manpowergroup.kintai.employee.application.dto.org.request.CompanyCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.org.request.CompanyUpdateRequest;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;

public final class CompanyAssembler {

    private CompanyAssembler() {
    }

    /**
     * 入力データを会社作成コマンドへ変換する。
     *
     * @param request 変換対象の会社作成リクエスト
     * @return 変換後の会社作成コマンド
     */
    public static CompanyCreateCommand toCommand(CompanyCreateRequest request) {
        return new CompanyCreateCommand(
                request.getParentId(),
                request.getName(),
                request.getCompanyCode(),
                request.getLevel(),
                request.getSort(),
                request.getStatus()
        );
    }

    /**
     * 入力データを会社更新コマンドへ変換する。
     *
     * @param request 変換対象の会社更新リクエスト
     * @return 変換後の会社更新コマンド
     */
    public static CompanyUpdateCommand toCommand(CompanyUpdateRequest request) {
        return new CompanyUpdateCommand(
                request.getParentId(),
                request.getName(),
                request.getCompanyCode(),
                request.getLevel(),
                request.getSort(),
                request.getStatus()
        );
    }

    /**
     * 入力データを会社レスポンスへ変換する。
     *
     * @param company 処理対象の会社
     * @return 変換後の会社レスポンス
     */
    public static CompanyResponse toResponse(OrgCompany company) {
        return CompanyResponse.from(company);
    }
}
