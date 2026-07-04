package com.manpowergroup.kintai.system.application.service.impl.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.system.application.query.manager.SubordinateQuery;
import com.manpowergroup.kintai.system.application.service.manager.ManagerSubordinateService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.system.domain.repository.manager.ManagerSubordinateRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeePositionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgGradeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeClosureMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerSubordinateServiceImpl implements ManagerSubordinateService {

    private final ManagerSubordinateRepository managerSubordinateRepository;

    @Override
    public JoinPageResult<SubordinateEmployeeResponse> pageSubordinates(SubordinateQuery query, int pageNum, int pageSize) {
        return managerSubordinateRepository.pageSubordinates(query, pageNum, pageSize);
    }
}
