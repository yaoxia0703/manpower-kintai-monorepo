package com.manpowergroup.kintai.employee.domain.repository.org;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;

import java.util.List;

public interface OrgCompanyRepository {

    List<OrgCompany> findList();

    Long selectCount(Long companyId);

    OrgCompany findById(Long id);

    PageResult<OrgCompany> findPage(int page, int size);

    List<OrgCompany> listEnabled();

    Long selecCountByCompanyCode(String companyCode);

    boolean existsByCompanyAndCodeExcludingId(String companyCode, Long id);

    void save(OrgCompany orgCompany);

    void updateById(OrgCompany orgCompany);

    void deleteById(Long id);

}
