package com.manpowergroup.kintai.system.application.service.manager;

import com.manpowergroup.kintai.system.application.dto.manager.SubordinateEmployeeResponse;

import java.util.List;

public interface ManagerSubordinateService {

    List<SubordinateEmployeeResponse> listSubordinates(Long managerEmployeeId);
}
