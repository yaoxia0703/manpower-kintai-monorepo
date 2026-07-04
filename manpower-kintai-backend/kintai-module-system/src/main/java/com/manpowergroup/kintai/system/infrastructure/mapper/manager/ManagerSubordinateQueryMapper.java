package com.manpowergroup.kintai.system.infrastructure.mapper.manager;

import com.manpowergroup.kintai.system.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.system.application.query.manager.SubordinateQuery;
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
}
