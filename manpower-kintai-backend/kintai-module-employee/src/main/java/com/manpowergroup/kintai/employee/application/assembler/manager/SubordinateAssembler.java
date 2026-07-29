package com.manpowergroup.kintai.employee.application.assembler.manager;

import com.manpowergroup.kintai.employee.application.dto.manager.request.SubordinateQueryRequest;
import com.manpowergroup.kintai.employee.application.query.manager.SubordinateQuery;

public class SubordinateAssembler {
    private SubordinateAssembler (){};

    /**
     * 入力データを部下情報検索条件へ変換する。
     *
     * @param request 変換対象の部下情報検索リクエスト
     * @param managerId 管理者の社員ID
     * @return 変換後の部下情報検索条件
     */
    public static SubordinateQuery toQuery(SubordinateQueryRequest request, Long managerId) {
        return new SubordinateQuery(
                managerId,             // ← ログイン者から注入
                request.getKeyword(),
                request.getNodeId(),
                request.getGradeId(),
                request.getStatus()
        );
    }
}
