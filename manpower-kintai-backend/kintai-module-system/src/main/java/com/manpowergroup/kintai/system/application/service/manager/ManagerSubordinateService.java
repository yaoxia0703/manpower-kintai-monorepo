package com.manpowergroup.kintai.system.application.service.manager;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.system.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.system.application.query.manager.SubordinateQuery;

import java.util.List;

public interface ManagerSubordinateService {

    JoinPageResult<SubordinateEmployeeResponse> pageSubordinates(SubordinateQuery query, int pageNum, int pageSize);


}
