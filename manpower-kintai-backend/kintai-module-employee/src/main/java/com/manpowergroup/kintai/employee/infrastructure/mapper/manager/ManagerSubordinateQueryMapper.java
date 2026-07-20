package com.manpowergroup.kintai.employee.infrastructure.mapper.manager;

import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateFilterOptionsResponse;
import com.manpowergroup.kintai.employee.application.query.manager.SubordinateQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManagerSubordinateQueryMapper {

    List<SubordinateEmployeeResponse> selectSubordinatePage(
            @Param("q") SubordinateQuery query,
            @Param("offset") int offset,
            @Param("size") int size);
    long countSubordinatePage(@Param("q") SubordinateQuery query);

    List<SubordinateFilterOptionsResponse.NodeOption> selectSubordinateNodeOptions(
            @Param("q") SubordinateQuery query);

    List<SubordinateFilterOptionsResponse.GradeOption> selectSubordinateGradeOptions(
            @Param("q") SubordinateQuery query);
}
