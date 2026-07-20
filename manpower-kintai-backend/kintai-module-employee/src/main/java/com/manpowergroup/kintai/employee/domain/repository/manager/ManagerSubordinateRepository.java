package com.manpowergroup.kintai.employee.domain.repository.manager;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateFilterOptionsResponse;
import com.manpowergroup.kintai.employee.application.query.manager.SubordinateQuery;


public interface ManagerSubordinateRepository {
    // 配下従業員のページング検索
    JoinPageResult<SubordinateEmployeeResponse> pageSubordinates(
            SubordinateQuery query, int pageNum, int pageSize);

    SubordinateFilterOptionsResponse filterOptions(Long managerId);
}
