package com.manpowergroup.kintai.employee.application.service.impl.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.employee.application.command.org.GradeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.GradeUpdateCommand;
import com.manpowergroup.kintai.employee.application.service.org.OrgGradeService;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgGradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 職級マスタサービス実装（アプリケーション層）
@Service
public class OrgGradeServiceImpl implements OrgGradeService {

    private final OrgGradeRepository orgGradeRepository;

    public OrgGradeServiceImpl(OrgGradeRepository orgGradeRepository) {
        this.orgGradeRepository = orgGradeRepository;
    }

    @Override
    public OrgGrade getById(Long id) {
        OrgGrade grade = orgGradeRepository.getById(id);
        if (grade == null) throw new BizException(SystemErrorCode.GRADE_NOT_FOUND);
        return grade;
    }

    @Override
    public PageResult<OrgGrade> pageByCompany(Long companyId, PageRequest request) {
        return orgGradeRepository.findPageByCompany(companyId, request.page(), request.size());
    }

    @Override
    public List<OrgGrade> listByCompany(Long companyId) {
        return orgGradeRepository.findByCompany(companyId);
    }

    @Override
    public List<OrgGrade> listByGradeLevel(String gradeLevel) {
        return orgGradeRepository.findByGradeLevel(gradeLevel);
    }

    @Override
    @Transactional
    public OrgGrade create(GradeCreateCommand command) {
        boolean exists = orgGradeRepository.selectCountByCodeAndCompany(command.code(), command.companyId());
        if (exists) throw new BizException(SystemErrorCode.GRADE_CODE_DUPLICATE);
        OrgGrade grade = OrgGrade.create(
            command.companyId(),
            command.name(),
            command.code(),
            command.gradeLevel(),
            command.sort(),
            command.status());
        orgGradeRepository.save(grade);
        return grade;
    }

    @Override
    @Transactional
    public OrgGrade update(Long id, GradeUpdateCommand command) {
        OrgGrade existing = getById(id);
        boolean exists = orgGradeRepository.existsByCompanyAndCodeExcludingId(id, command.companyId(), command.code());
        if (exists) throw new BizException(SystemErrorCode.GRADE_CODE_DUPLICATE);
        existing.updateEditableFields(
            command.name(),
            command.code(),
            command.gradeLevel(),
            command.sort());
        orgGradeRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        OrgGrade grade = getById(id);
        grade.enable();
        orgGradeRepository.updateById(grade);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        OrgGrade grade = getById(id);
        grade.disable();
        orgGradeRepository.updateById(grade);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        orgGradeRepository.deleteById(id);
    }

    @Override
    public boolean gradeBelongsToCompany(long gradeId, long companyId) {
        return orgGradeRepository.selectCountByCompanyAndGrade(companyId, gradeId) > 0;
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

        @Override
        public int code() {
            return code;
        }

        @Override
        public String messageKey() {
            return messageKey;
        }
    }
}
