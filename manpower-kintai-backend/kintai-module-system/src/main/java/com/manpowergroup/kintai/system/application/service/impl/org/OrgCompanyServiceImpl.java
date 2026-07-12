package com.manpowergroup.kintai.system.application.service.impl.org;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.org.CompanyCreateCommand;
import com.manpowergroup.kintai.system.application.command.org.CompanyUpdateCommand;
import com.manpowergroup.kintai.system.application.service.org.OrgCompanyService;
import com.manpowergroup.kintai.system.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgCompanyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 会社マスタサービス実装（アプリケーション層）
@Service
public class OrgCompanyServiceImpl extends ServiceImpl<OrgCompanyMapper, OrgCompany>
        implements OrgCompanyService {

    @Override
    public OrgCompany getById(Long id) {
        OrgCompany company = super.getById(id);
        if (company == null) throw new BizException(SystemErrorCode.COMPANY_NOT_FOUND);
        return company;
    }

    @Override
    public PageResult<OrgCompany> page(PageRequest request) {
        Page<OrgCompany> p = new Page<>(request.page(), request.size());
        page(p, lambdaQuery().orderByAsc(OrgCompany::getSort).getWrapper());
        return PageResult.of(p);
    }

    @Override
    public List<OrgCompany> listEnabled() {
        return lambdaQuery()
                .eq(OrgCompany::getStatus, Status.ENABLED)
                .orderByAsc(OrgCompany::getSort)
                .list();
    }

    @Override
    @Transactional
    public OrgCompany create(CompanyCreateCommand command) {
        boolean exists = lambdaQuery().eq(OrgCompany::getCompanyCode, command.companyCode()).count() > 0;
        if (exists) throw new BizException(SystemErrorCode.COMPANY_CODE_DUPLICATE);
        OrgCompany company = OrgCompany.create(
                command.parentId(),
                command.name(),
                command.companyCode(),
                command.level(),
                command.sort(),
                command.status());
        save(company);
        return company;
    }

    @Override
    @Transactional
    public OrgCompany update(Long id, CompanyUpdateCommand command) {
        OrgCompany existing = getById(id);
        boolean exists = lambdaQuery()
                .eq(OrgCompany::getCompanyCode, command.companyCode())
                .ne(OrgCompany::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.COMPANY_CODE_DUPLICATE);
        existing.updateEditableFields(
                command.parentId(),
                command.name(),
                command.companyCode(),
                command.level(),
                command.sort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        OrgCompany company = getById(id);
        company.enable();
        updateById(company);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        OrgCompany company = getById(id);
        company.disable();
        updateById(company);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        COMPANY_NOT_FOUND(404, "error.company.not_found"),
        COMPANY_CODE_DUPLICATE(409, "error.company.code_duplicate");

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

