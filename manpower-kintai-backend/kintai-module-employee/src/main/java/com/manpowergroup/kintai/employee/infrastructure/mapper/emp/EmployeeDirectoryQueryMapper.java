package com.manpowergroup.kintai.employee.infrastructure.mapper.emp;

import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryFilter;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeDirectoryQueryMapper {

    /**
     * HR用全社員一覧ページング
     *
     * @param query 検索条件
     * @param page
     * @param size
     * @return
     */
    List<EmployeeDirectoryResponse> pageDirectory(@Param("q") EmployeeDirectoryFilter filter, @Param("page") int page, @Param("size") int size);

    Long countDirectory(@Param("q") EmployeeDirectoryFilter filter);
}
