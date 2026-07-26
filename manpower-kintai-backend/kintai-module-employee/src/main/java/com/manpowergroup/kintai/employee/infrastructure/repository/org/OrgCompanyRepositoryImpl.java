package com.manpowergroup.kintai.employee.infrastructure.repository.org;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgCompanyRepository;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgCompanyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrgCompanyRepositoryImpl implements OrgCompanyRepository {

    private final OrgCompanyMapper orgCompanyMapper;

    public OrgCompanyRepositoryImpl(OrgCompanyMapper orgCompanyMapper) {
        this.orgCompanyMapper = orgCompanyMapper;
    }

    @Override
    public List<OrgCompany> findList() {
        return orgCompanyMapper.selectList(new LambdaQueryWrapper<OrgCompany>()
            .eq(OrgCompany::getStatus, Status.ENABLED)
            .orderByAsc(OrgCompany::getLevel)
            .orderByAsc(OrgCompany::getSort));
    }

    @Override
    public Long selectCount(Long companyId) {
        return orgCompanyMapper.selectCount(new LambdaQueryWrapper<OrgCompany>()
            .eq(OrgCompany::getStatus, Status.ENABLED)
            .eq(OrgCompany::getId, companyId));
    }

    @Override
    public OrgCompany findById(Long id) {
        return orgCompanyMapper.selectById(id);
    }

    @Override
    public PageResult<OrgCompany> findPage(int page, int size) {
        Page<OrgCompany> p = new Page<>(page, size);
        orgCompanyMapper.selectPage(p, Wrappers.<OrgCompany>lambdaQuery()
            .orderByAsc(OrgCompany::getSort));
        return PageResult.of(p);
    }

    @Override
    public List<OrgCompany> listEnabled() {
        return orgCompanyMapper.selectList(new LambdaQueryWrapper<OrgCompany>()
            .eq(OrgCompany::getStatus, Status.ENABLED)
            .orderByAsc(OrgCompany::getSort));
    }

    @Override
    public Long selecCountByCompanyCode(String companyCode) {
        return orgCompanyMapper.selectCount(new LambdaQueryWrapper<OrgCompany>()
            .eq(OrgCompany::getCompanyCode, companyCode));
    }

    @Override
    public boolean existsByCompanyAndCodeExcludingId(String companyCode, Long id) {
        return orgCompanyMapper.selectCount(new LambdaQueryWrapper<OrgCompany>()
            .eq(OrgCompany::getCompanyCode, companyCode)
            .ne(OrgCompany::getId, id)) > 0;
    }

    @Override
    public void save(OrgCompany orgCompany) {
        orgCompanyMapper.insert(orgCompany);
    }

    @Override
    public void updateById(OrgCompany orgCompany) {
        orgCompanyMapper.updateById(orgCompany);
    }

    @Override
    public void deleteById(Long id) {
        orgCompanyMapper.deleteById(id);
    }
}
