package com.manpowergroup.kintai.system.domain.repository.manager;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.system.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.system.application.query.manager.SubordinateQuery;


public interface ManagerSubordinateRepository {
    // 配下従業員のページング検索
    JoinPageResult<SubordinateEmployeeResponse> pageSubordinates(
            SubordinateQuery query, int pageNum, int pageSize);

}
