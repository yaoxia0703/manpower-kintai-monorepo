package com.manpowergroup.kintai.employee.infrastructure.repository.org;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgGradeRepository;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgGradeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrgGradeRepositoryImpl implements OrgGradeRepository {
    private final OrgGradeMapper orgGradeMapper;

    public OrgGradeRepositoryImpl(OrgGradeMapper orgGradeMapper) {
        this.orgGradeMapper = orgGradeMapper;
    }

    @Override
    public List<OrgGrade> findListByCompany(Long companyId) {
        return orgGradeMapper.selectList(new LambdaQueryWrapper<OrgGrade>()
            .eq(OrgGrade::getCompanyId, companyId)
            .eq(OrgGrade::getStatus, Status.ENABLED)
            .orderByAsc(OrgGrade::getSort));
    }

    @Override
    public Long selectCountByCompanyAndGrade(Long companyId, Long gradeId) {
        return orgGradeMapper.selectCount(new LambdaQueryWrapper<OrgGrade>()
            .eq(OrgGrade::getId, gradeId)
            .eq(OrgGrade::getCompanyId, companyId)
            .eq(OrgGrade::getStatus, Status.ENABLED));
    }

    @Override
    public OrgGrade getById(Long id) {
        return orgGradeMapper.selectById(id);
    }

    @Override
    public PageResult<OrgGrade> findPageByCompany(Long companyId, int page, int size) {
        Page<OrgGrade> p = new Page<>(page, size);
        orgGradeMapper.selectPage(p, Wrappers.<OrgGrade>lambdaQuery()
            .eq(OrgGrade::getCompanyId, companyId)
            .orderByAsc(OrgGrade::getSort));
        return PageResult.of(p);
    }

    @Override
    public List<OrgGrade> findByCompany(Long companyId) {
        return orgGradeMapper.selectList(new LambdaQueryWrapper<OrgGrade>()
            .eq(OrgGrade::getCompanyId, companyId)
            .orderByAsc(OrgGrade::getSort)
        );
    }

    @Override
    public List<OrgGrade> findByGradeLevel(String gradeLevel) {
        return orgGradeMapper.selectList(new LambdaQueryWrapper<OrgGrade>()
            .eq(OrgGrade::getGradeLevel, gradeLevel)
            .orderByAsc(OrgGrade::getSort)
        );
    }

    @Override
    public boolean selectCountByCodeAndCompany(String code, Long companyId) {
        return orgGradeMapper.selectCount(new LambdaQueryWrapper<OrgGrade>()
            .eq(OrgGrade::getCompanyId, companyId)
            .eq(OrgGrade::getCode, code)
        ) > 0;
    }

    @Override
    public void save(OrgGrade grade) {
        orgGradeMapper.insert(grade);
    }

    @Override
    public void updateById(OrgGrade grade) {
        orgGradeMapper.updateById(grade);
    }

    @Override
    public void deleteById(Long id) {
        orgGradeMapper.deleteById(id);
    }

    @Override
    public boolean existsByCompanyAndCodeExcludingId(Long id, Long companyId, String code) {
        return orgGradeMapper.selectCount(new LambdaQueryWrapper<OrgGrade>()
            .eq(OrgGrade::getCompanyId, companyId)
            .eq(OrgGrade::getCode, code)
            .ne(OrgGrade::getId, id)
        ) > 0;
    }

}
