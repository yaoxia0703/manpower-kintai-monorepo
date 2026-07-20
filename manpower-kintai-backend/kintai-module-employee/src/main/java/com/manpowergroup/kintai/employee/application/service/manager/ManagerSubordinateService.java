package com.manpowergroup.kintai.employee.application.service.manager;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateFilterOptionsResponse;
import com.manpowergroup.kintai.employee.application.query.manager.SubordinateQuery;

public interface ManagerSubordinateService {

    JoinPageResult<SubordinateEmployeeResponse> pageSubordinates(SubordinateQuery query, int pageNum, int pageSize);

    SubordinateFilterOptionsResponse options(Long managerId);
}
