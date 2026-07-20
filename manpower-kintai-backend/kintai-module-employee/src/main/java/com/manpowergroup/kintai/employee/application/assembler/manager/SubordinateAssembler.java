package com.manpowergroup.kintai.employee.application.assembler.manager;

import com.manpowergroup.kintai.employee.application.dto.manager.request.SubordinateQueryRequest;
import com.manpowergroup.kintai.employee.application.query.manager.SubordinateQuery;

public class SubordinateAssembler {
    private SubordinateAssembler (){};

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
