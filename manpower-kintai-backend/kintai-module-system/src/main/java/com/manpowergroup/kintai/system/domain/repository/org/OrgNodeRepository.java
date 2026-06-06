package com.manpowergroup.kintai.system.domain.repository.org;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;

import java.util.List;
import java.util.Optional;

public interface OrgNodeRepository {

    Optional<OrgNode> findById(Long id);

    boolean existsByCompanyAndCode(Long companyId, String code);

    boolean existsByCompanyAndCodeExcludingId(Long companyId, String code, Long excludeId);

    PageResult<OrgNode> findPageByCompany(Long companyId, int page, int size);

    List<OrgNode> listEnabledByCompany(Long companyId);

    OrgNode save(OrgNode node);

    OrgNode update(OrgNode node);

    void deleteById(Long id);
}
