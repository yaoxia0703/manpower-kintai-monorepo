package com.manpowergroup.kintai.system.application.service.impl.org;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgGradeMapper;
import com.manpowergroup.kintai.system.application.service.org.OrgGradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 職級マスタサービス実装（アプリケーション層）
@Service
public class OrgGradeServiceImpl extends ServiceImpl<OrgGradeMapper, OrgGrade>
        implements OrgGradeService {

    @Override
    public OrgGrade getById(Long id) {
        OrgGrade grade = super.getById(id);
        if (grade == null) throw new BizException(SystemErrorCode.GRADE_NOT_FOUND);
        return grade;
    }

    @Override
    public PageResult<OrgGrade> pageByCompany(Long companyId, PageRequest request) {
        Page<OrgGrade> p = new Page<>(request.page(), request.size());
        page(p, lambdaQuery()
                .eq(OrgGrade::getCompanyId, companyId)
                .orderByAsc(OrgGrade::getSort)
                .getWrapper());
        return PageResult.of(p);
    }

    @Override
    public List<OrgGrade> listByCompany(Long companyId) {
        return lambdaQuery()
                .eq(OrgGrade::getCompanyId, companyId)
                .orderByAsc(OrgGrade::getSort)
                .list();
    }

    @Override
    public List<OrgGrade> listByGradeLevel(String gradeLevel) {
        return lambdaQuery()
                .eq(OrgGrade::getGradeLevel, gradeLevel)
                .orderByAsc(OrgGrade::getSort)
                .list();
    }

    @Override
    @Transactional
    public OrgGrade create(OrgGrade grade) {
        boolean exists = lambdaQuery()
                .eq(OrgGrade::getCompanyId, grade.getCompanyId())
                .eq(OrgGrade::getCode, grade.getCode())
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.GRADE_CODE_DUPLICATE);
        save(grade);
        return grade;
    }

    @Override
    @Transactional
    public OrgGrade update(Long id, OrgGrade grade) {
        OrgGrade existing = getById(id);
        boolean exists = lambdaQuery()
                .eq(OrgGrade::getCompanyId, grade.getCompanyId())
                .eq(OrgGrade::getCode, grade.getCode())
                .ne(OrgGrade::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.GRADE_CODE_DUPLICATE);
        existing.setName(grade.getName())
                .setCode(grade.getCode())
                .setGradeLevel(grade.getGradeLevel())
                .setSort(grade.getSort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        OrgGrade grade = getById(id);
        grade.setStatus(Status.ENABLED);
        updateById(grade);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        OrgGrade grade = getById(id);
        grade.setStatus(Status.DISABLED);
        updateById(grade);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        GRADE_NOT_FOUND(404, "error.grade.not_found"),
        GRADE_CODE_DUPLICATE(409, "error.grade.code_duplicate");

        private final int code;
        private final String messageKey;

        SystemErrorCode(int code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}

